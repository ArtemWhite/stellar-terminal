package com.artemwhite.satellite.adapter.inbound.web

import com.artemwhite.satellite.application.usecase.GetPositionUseCase
import com.artemwhite.satellite.application.usecase.GetTimelineUseCase
import com.artemwhite.satellite.application.usecase.PredictPassUseCase
import com.artemwhite.satellite.domain.exceptions.ObjectNotFoundException
import com.artemwhite.satellite.domain.models.OrbitSnapshot
import com.artemwhite.satellite.domain.ports.outbound.Clock
import com.artemwhite.satellite.domain.ports.outbound.PositionCalculator
import com.artemwhite.satellite.testutil.FakeObjectRegistryClient
import com.artemwhite.satellite.testutil.FakeSatelliteRepository
import com.artemwhite.satellite.testutil.SatelliteFixtures.ISS_OBJECT_ID
import com.artemwhite.satellite.testutil.SatelliteFixtures.ISS_TLE
import com.artemwhite.satellite.testutil.SatelliteFixtures.NOW
import com.artemwhite.satellite.testutil.SatelliteFixtures.SAMPLE_POSITION
import com.artemwhite.satellite.testutil.SatelliteFixtures.objectRef
import com.artemwhite.satellite.testutil.SatelliteFixtures.satellite
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class SatelliteControllerTest {

    private val repository = FakeSatelliteRepository()
    private val registry = FakeObjectRegistryClient()
    private val clock = Clock { NOW }

    private val controller = SatelliteController(
        GetPositionUseCase(repository, registry, PositionCalculator { _, _ -> SAMPLE_POSITION }, clock),
        PredictPassUseCase(repository, PositionCalculator { _, _ -> SAMPLE_POSITION }, clock),
        GetTimelineUseCase(repository),
    )

    private fun seed() {
        repository.saveSatellite(satellite())
        repository.saveTle(ISS_OBJECT_ID, ISS_TLE)
        registry.objects[ISS_OBJECT_ID] = objectRef()
    }

    @Test
    fun `position returns a state vector response`() {
        seed()

        val response = controller.position(ISS_OBJECT_ID, null)

        assertThat(response.x).isEqualTo(SAMPLE_POSITION.x)
        assertThat(response.y).isEqualTo(SAMPLE_POSITION.y)
        assertThat(response.z).isEqualTo(SAMPLE_POSITION.z)
        assertThat(response.vx).isEqualTo(SAMPLE_POSITION.vx)
        assertThat(response.vy).isEqualTo(SAMPLE_POSITION.vy)
        assertThat(response.vz).isEqualTo(SAMPLE_POSITION.vz)
    }

    @Test
    fun `timeline returns stored snapshots as a page of responses`() {
        seed()
        repository.saveSnapshot(ISS_OBJECT_ID, OrbitSnapshot(SAMPLE_POSITION, NOW))

        val response = controller.timeline(ISS_OBJECT_ID, 0, 20)

        assertThat(response.items).hasSize(1)
        assertThat(response.total).isEqualTo(1)
        assertThat(response.items[0].calculatedAt).isEqualTo(NOW)
        assertThat(response.items[0].position.x).isEqualTo(SAMPLE_POSITION.x)
    }

    @Test
    fun `passes builds the query and returns predictions`() {
        seed()

        val response = controller.passes(ISS_OBJECT_ID, 10.0, 300, 60)

        // SAMPLE_POSITION is below the north-pole station's horizon, so no pass is predicted.
        assertThat(response).isEmpty()
    }

    @Test
    fun `position throws for an unknown object`() {
        assertThatThrownBy { controller.position(UUID.randomUUID(), null) }
            .isInstanceOf(ObjectNotFoundException::class.java)
    }
}
