package com.artemwhite.satellite.application.usecase

import com.artemwhite.satellite.domain.exceptions.SatelliteNotFoundException
import com.artemwhite.satellite.domain.models.GroundStation
import com.artemwhite.satellite.domain.models.PassPrediction
import com.artemwhite.satellite.domain.models.PassQuery
import com.artemwhite.satellite.domain.ports.outbound.Clock
import com.artemwhite.satellite.domain.ports.outbound.PositionCalculator
import com.artemwhite.satellite.domain.ports.outbound.SatelliteRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class PredictPassUseCase(
    private val repository: SatelliteRepository,
    private val calculator: PositionCalculator,
    private val clock: Clock,
) {
    fun execute(
        objectId: UUID,
        query: PassQuery,
        station: GroundStation = GroundStation.northPole(),
    ): List<PassPrediction> {
        val tle = repository.findTle(objectId) ?: throw SatelliteNotFoundException(objectId)
        val start = clock.now()
        val end = start.plusSeconds(query.windowSeconds)
        val sampleTimes = generateSequence(start) { it.plusSeconds(query.stepSeconds) }
            .takeWhile { !it.isAfter(end) }
            .toList()

        val passes = mutableListOf<PassPrediction>()
        var passStart: Instant? = null
        var maxElevation = Double.NEGATIVE_INFINITY

        for (t in sampleTimes) {
            val elevation = station.elevationDeg(calculator.propagate(tle, t))
            if (elevation >= query.minElevationDeg) {
                if (passStart == null) {
                    passStart = t
                    maxElevation = elevation
                } else {
                    maxElevation = maxOf(maxElevation, elevation)
                }
            } else if (passStart != null) {
                passes += PassPrediction(objectId, passStart, t, maxElevation)
                passStart = null
                maxElevation = Double.NEGATIVE_INFINITY
            }
        }
        passStart?.let { passes += PassPrediction(objectId, it, sampleTimes.last(), maxElevation) }
        return passes
    }
}
