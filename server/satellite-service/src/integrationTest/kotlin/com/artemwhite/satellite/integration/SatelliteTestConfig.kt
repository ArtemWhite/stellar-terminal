package com.artemwhite.satellite.integration

import com.artemwhite.satellite.domain.exceptions.ObjectNotFoundException
import com.artemwhite.satellite.domain.models.ActiveTle
import com.artemwhite.satellite.domain.ports.outbound.EventPublisher
import com.artemwhite.satellite.domain.ports.outbound.ObjectRegistryClient
import com.artemwhite.satellite.domain.ports.outbound.TleProvider
import com.artemwhite.shared.model.ObjectRef
import com.artemwhite.shared.model.SatelliteEvent
import com.artemwhite.shared.model.enums.ObjectStatus
import java.util.UUID
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

/**
 * Replaces the outbound HTTP ports with in-memory stubs for the integration
 * tests. The `@Primary` beans shadow the real RestClient adapters so the
 * DB-backed tests never reach the catalog, CelesTrak or the event bus.
 */
@TestConfiguration
class SatelliteTestConfig {

    class StubObjectRegistryClient : ObjectRegistryClient {
        val objects = LinkedHashMap<UUID, ObjectRef>()
        val statusUpdates = mutableListOf<Pair<UUID, ObjectStatus>>()

        override fun getObject(objectId: UUID): ObjectRef =
            objects[objectId] ?: throw ObjectNotFoundException(objectId)

        override fun updateStatus(objectId: UUID, status: ObjectStatus) {
            statusUpdates += objectId to status
        }
    }

    class StubTleProvider : TleProvider {
        val tles = mutableListOf<ActiveTle>()

        override fun fetchActiveSatellites(): List<ActiveTle> = tles.toList()
    }

    class RecordingEventPublisher : EventPublisher {
        val events = mutableListOf<SatelliteEvent>()

        override fun publish(event: SatelliteEvent) {
            events += event
        }
    }

    @Bean
    @Primary
    fun objectRegistryClient(): StubObjectRegistryClient = StubObjectRegistryClient()

    @Bean
    @Primary
    fun tleProvider(): StubTleProvider = StubTleProvider()

    @Bean
    @Primary
    fun eventPublisher(): RecordingEventPublisher = RecordingEventPublisher()
}
