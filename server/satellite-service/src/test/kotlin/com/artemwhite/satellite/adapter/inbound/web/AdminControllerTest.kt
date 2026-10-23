package com.artemwhite.satellite.adapter.inbound.web

import com.artemwhite.satellite.adapter.inbound.web.dto.RegisterSatelliteRequest
import com.artemwhite.satellite.application.usecase.RegisterSatelliteUseCase
import com.artemwhite.satellite.application.usecase.SyncSatellitesUseCase
import com.artemwhite.satellite.domain.ports.outbound.Clock
import com.artemwhite.satellite.domain.ports.outbound.EventPublisher
import com.artemwhite.satellite.domain.ports.outbound.PositionCalculator
import com.artemwhite.satellite.domain.ports.outbound.TleProvider
import com.artemwhite.satellite.testutil.FakeObjectRegistryClient
import com.artemwhite.satellite.testutil.FakeSatelliteRepository
import com.artemwhite.satellite.testutil.SatelliteFixtures.ISS_OBJECT_ID
import com.artemwhite.satellite.testutil.SatelliteFixtures.NORAD_ISS
import com.artemwhite.satellite.testutil.SatelliteFixtures.NOW
import com.artemwhite.satellite.testutil.SatelliteFixtures.SAMPLE_POSITION
import com.artemwhite.satellite.testutil.SatelliteFixtures.activeTle
import com.artemwhite.satellite.testutil.SatelliteFixtures.satellite
import com.artemwhite.shared.model.enums.ObjectStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AdminControllerTest {

    private val repository = FakeSatelliteRepository()
    private val registry = FakeObjectRegistryClient()
    private val clock = Clock { NOW }

    private val controller = AdminController(
        SyncSatellitesUseCase(
            repository = repository,
            tleProvider = TleProvider { listOf(activeTle(NORAD_ISS)) },
            calculator = PositionCalculator { _, _ -> SAMPLE_POSITION },
            registry = registry,
            eventPublisher = EventPublisher { },
            clock = clock,
        ),
        RegisterSatelliteUseCase(repository),
    )

    @Test
    fun `register creates a satellite and returns it`() {
        val response = controller.register(RegisterSatelliteRequest(ISS_OBJECT_ID, NORAD_ISS))

        assertThat(response.objectId).isEqualTo(ISS_OBJECT_ID)
        assertThat(response.noradId).isEqualTo(NORAD_ISS)
        assertThat(response.status).isEqualTo(ObjectStatus.ACTIVE)
        assertThat(repository.findSatellite(ISS_OBJECT_ID)).isNotNull
    }

    @Test
    fun `sync returns a report`() {
        repository.saveSatellite(satellite())

        val response = controller.sync()

        assertThat(response.updated).isEqualTo(1)
        assertThat(response.failed).isZero()
        assertThat(response.decayed).isZero()
    }
}
