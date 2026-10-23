package com.artemwhite.satellite.application.usecase

import com.artemwhite.satellite.domain.models.OrbitSnapshot
import com.artemwhite.satellite.domain.models.SyncReport
import com.artemwhite.satellite.domain.ports.outbound.Clock
import com.artemwhite.satellite.domain.ports.outbound.EventPublisher
import com.artemwhite.satellite.domain.ports.outbound.ObjectRegistryClient
import com.artemwhite.satellite.domain.ports.outbound.PositionCalculator
import com.artemwhite.satellite.domain.ports.outbound.SatelliteRepository
import com.artemwhite.satellite.domain.ports.outbound.TleProvider
import com.artemwhite.shared.model.SatelliteEvent
import com.artemwhite.shared.model.enums.EventType
import com.artemwhite.shared.model.enums.ObjectStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SyncSatellitesUseCase(
    private val repository: SatelliteRepository,
    private val tleProvider: TleProvider,
    private val calculator: PositionCalculator,
    private val registry: ObjectRegistryClient,
    private val eventPublisher: EventPublisher,
    private val clock: Clock,
) {
    private val log = LoggerFactory.getLogger(SyncSatellitesUseCase::class.java)

    fun sync(): SyncReport {
        val now = clock.now()
        val activeByNoradId = tleProvider.fetchActiveSatellites().associateBy { it.noradId }
        var updated = 0
        var failed = 0
        var decayed = 0

        for (satellite in repository.findAll()) {
            val activeTle = activeByNoradId[satellite.noradId]
            if (activeTle == null) {
                if (satellite.status == ObjectStatus.ACTIVE) {
                    satellite.changeStatus(ObjectStatus.DECAYED)
                    repository.saveSatellite(satellite)
                    registry.updateStatus(satellite.objectId, ObjectStatus.DECAYED)
                    eventPublisher.publish(
                        SatelliteEvent(satellite.objectId, ObjectStatus.DECAYED, EventType.DECAYED, now),
                    )
                    decayed++
                }
            } else {
                try {
                    val position = calculator.propagate(activeTle.tle, now)
                    repository.saveTle(satellite.objectId, activeTle.tle)
                    repository.saveSnapshot(satellite.objectId, OrbitSnapshot(position, now))
                    updated++
                } catch (_: RuntimeException) {
                    failed++
                }
            }
        }
        log.info("Sync finished: {} updated, {} decayed, {} failed", updated, decayed, failed)
        return SyncReport(updated = updated, failed = failed, decayed = decayed)
    }
}
