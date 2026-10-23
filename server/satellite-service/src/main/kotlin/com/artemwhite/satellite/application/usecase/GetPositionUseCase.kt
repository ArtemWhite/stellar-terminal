package com.artemwhite.satellite.application.usecase

import com.artemwhite.satellite.domain.exceptions.NotASatelliteException
import com.artemwhite.satellite.domain.exceptions.SatelliteNotFoundException
import com.artemwhite.satellite.domain.models.Position
import com.artemwhite.satellite.domain.ports.outbound.Clock
import com.artemwhite.satellite.domain.ports.outbound.ObjectRegistryClient
import com.artemwhite.satellite.domain.ports.outbound.PositionCalculator
import com.artemwhite.satellite.domain.ports.outbound.SatelliteRepository
import com.artemwhite.shared.model.enums.ObjectCategory
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class GetPositionUseCase(
    private val repository: SatelliteRepository,
    private val registry: ObjectRegistryClient,
    private val calculator: PositionCalculator,
    private val clock: Clock,
) {
    fun execute(objectId: UUID, at: Instant? = null): Position {
        val ref = registry.getObject(objectId)
        if (ref.category != ObjectCategory.SATELLITE) {
            throw NotASatelliteException(objectId, ref.category)
        }
        val tle = repository.findTle(objectId) ?: throw SatelliteNotFoundException(objectId)
        return calculator.propagate(tle, at ?: clock.now())
    }
}
