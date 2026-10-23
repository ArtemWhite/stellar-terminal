package com.artemwhite.satellite.application.usecase

import com.artemwhite.satellite.domain.models.ActiveTle
import com.artemwhite.satellite.domain.ports.outbound.Clock
import com.artemwhite.satellite.domain.ports.outbound.EventPublisher
import com.artemwhite.satellite.domain.ports.outbound.PositionCalculator
import com.artemwhite.satellite.domain.ports.outbound.TleProvider
import com.artemwhite.satellite.testutil.FakeObjectRegistryClient
import com.artemwhite.satellite.testutil.FakeSatelliteRepository
import com.artemwhite.satellite.testutil.SatelliteFixtures.NOW
import com.artemwhite.satellite.testutil.SatelliteFixtures.SAMPLE_POSITION
import com.artemwhite.satellite.testutil.SatelliteFixtures.activeTle
import com.artemwhite.satellite.testutil.SatelliteFixtures.satellite
import com.artemwhite.shared.model.SatelliteEvent
import com.artemwhite.shared.model.enums.EventType
import com.artemwhite.shared.model.enums.ObjectStatus
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SyncSatellitesUseCaseTest {

    private val repository = FakeSatelliteRepository()
    private val registry = FakeObjectRegistryClient()
    private val clock = Clock { NOW }
    private val published = mutableListOf<SatelliteEvent>()

    private val okCalculator = PositionCalculator { _, _ -> SAMPLE_POSITION }

    private fun useCase(tles: List<ActiveTle>, calculator: PositionCalculator) = SyncSatellitesUseCase(
        repository = repository,
        tleProvider = TleProvider { tles },
        calculator = calculator,
        registry = registry,
        eventPublisher = EventPublisher { published += it },
        clock = clock,
    )

    @Test
    fun `updates every satellite present in the fresh TLE set`() {
        val sat1 = satellite(UUID.randomUUID(), 25544)
        val sat2 = satellite(UUID.randomUUID(), 25545)
        repository.saveSatellite(sat1)
        repository.saveSatellite(sat2)

        val report = useCase(listOf(activeTle(25544), activeTle(25545)), okCalculator).sync()

        assertThat(report.updated).isEqualTo(2)
        assertThat(report.failed).isZero()
        assertThat(report.decayed).isZero()
        assertThat(repository.findTle(sat1.objectId)).isNotNull
        assertThat(repository.findTle(sat2.objectId)).isNotNull
        assertThat(repository.findSnapshots(sat1.objectId)).hasSize(1)
        assertThat(repository.findSnapshots(sat2.objectId)).hasSize(1)
    }

    @Test
    fun `marks a missing ACTIVE satellite as decayed and publishes an event`() {
        val present = satellite(UUID.randomUUID(), 25544)
        val missing = satellite(UUID.randomUUID(), 25545)
        repository.saveSatellite(present)
        repository.saveSatellite(missing)

        val report = useCase(listOf(activeTle(25544)), okCalculator).sync()

        assertThat(report.decayed).isEqualTo(1)
        assertThat(report.updated).isEqualTo(1)
        assertThat(repository.findSatellite(missing.objectId)!!.status).isEqualTo(ObjectStatus.DECAYED)
        assertThat(registry.statusUpdates).containsExactly(missing.objectId to ObjectStatus.DECAYED)
        assertThat(published).hasSize(1)
        assertThat(published[0].objectId).isEqualTo(missing.objectId)
        assertThat(published[0].type).isEqualTo(EventType.DECAYED)
        assertThat(published[0].status).isEqualTo(ObjectStatus.DECAYED)
        assertThat(published[0].occurredAt).isEqualTo(NOW)
    }

    @Test
    fun `leaves an already decayed missing satellite untouched`() {
        val decayed = satellite(UUID.randomUUID(), 25545, ObjectStatus.DECAYED)
        repository.saveSatellite(decayed)

        val report = useCase(emptyList(), okCalculator).sync()

        assertThat(report.decayed).isZero()
        assertThat(registry.statusUpdates).isEmpty()
        assertThat(published).isEmpty()
        assertThat(repository.findSatellite(decayed.objectId)!!.status).isEqualTo(ObjectStatus.DECAYED)
    }

    @Test
    fun `counts a failed propagation and continues without persisting it`() {
        val ok = satellite(UUID.randomUUID(), 25544)
        val bad = satellite(UUID.randomUUID(), 25545)
        repository.saveSatellite(ok)
        repository.saveSatellite(bad)

        val calculator = PositionCalculator { tle, _ ->
            if (tle.line1.contains("25545")) throw RuntimeException("boom")
            SAMPLE_POSITION
        }

        val report = useCase(listOf(activeTle(25544), activeTle(25545)), calculator).sync()

        assertThat(report.updated).isEqualTo(1)
        assertThat(report.failed).isEqualTo(1)
        assertThat(report.decayed).isZero()
        // Propagate runs before persist, so the malformed TLE is never stored.
        assertThat(repository.findTle(bad.objectId)).isNull()
        assertThat(repository.findSnapshots(bad.objectId)).isEmpty()
        assertThat(repository.findTle(ok.objectId)).isNotNull
    }
}
