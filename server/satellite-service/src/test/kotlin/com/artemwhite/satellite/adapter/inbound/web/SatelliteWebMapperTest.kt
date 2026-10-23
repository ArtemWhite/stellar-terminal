package com.artemwhite.satellite.adapter.inbound.web

import com.artemwhite.satellite.adapter.inbound.web.dto.RegisterSatelliteRequest
import com.artemwhite.satellite.adapter.inbound.web.mappers.toCommand
import com.artemwhite.satellite.adapter.inbound.web.mappers.toResponse
import com.artemwhite.satellite.domain.models.OrbitSnapshot
import com.artemwhite.satellite.domain.models.PassPrediction
import com.artemwhite.satellite.domain.models.SyncReport
import com.artemwhite.satellite.testutil.SatelliteFixtures.ISS_OBJECT_ID
import com.artemwhite.satellite.testutil.SatelliteFixtures.LATER
import com.artemwhite.satellite.testutil.SatelliteFixtures.NORAD_ISS
import com.artemwhite.satellite.testutil.SatelliteFixtures.NOW
import com.artemwhite.satellite.testutil.SatelliteFixtures.SAMPLE_POSITION
import com.artemwhite.satellite.testutil.SatelliteFixtures.satellite
import com.artemwhite.shared.model.enums.ObjectStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SatelliteWebMapperTest {

    @Test
    fun `maps a request DTO to a command`() {
        val request = RegisterSatelliteRequest(ISS_OBJECT_ID, NORAD_ISS)

        val command = request.toCommand()

        assertThat(command.objectId).isEqualTo(ISS_OBJECT_ID)
        assertThat(command.noradId).isEqualTo(NORAD_ISS)
    }

    @Test
    fun `maps a position to a response`() {
        val response = SAMPLE_POSITION.toResponse()

        assertThat(response.x).isEqualTo(SAMPLE_POSITION.x)
        assertThat(response.y).isEqualTo(SAMPLE_POSITION.y)
        assertThat(response.z).isEqualTo(SAMPLE_POSITION.z)
        assertThat(response.vx).isEqualTo(SAMPLE_POSITION.vx)
        assertThat(response.vy).isEqualTo(SAMPLE_POSITION.vy)
        assertThat(response.vz).isEqualTo(SAMPLE_POSITION.vz)
    }

    @Test
    fun `maps a pass prediction to a response`() {
        val pass = PassPrediction(ISS_OBJECT_ID, NOW, LATER, 42.0)

        val response = pass.toResponse()

        assertThat(response.objectId).isEqualTo(ISS_OBJECT_ID)
        assertThat(response.start).isEqualTo(NOW)
        assertThat(response.end).isEqualTo(LATER)
        assertThat(response.maxElevation).isEqualTo(42.0)
    }

    @Test
    fun `maps an orbit snapshot to a response`() {
        val snapshot = OrbitSnapshot(SAMPLE_POSITION, NOW)

        val response = snapshot.toResponse()

        assertThat(response.calculatedAt).isEqualTo(NOW)
        assertThat(response.position.x).isEqualTo(SAMPLE_POSITION.x)
    }

    @Test
    fun `maps a satellite to a response`() {
        val response = satellite().toResponse()

        assertThat(response.objectId).isEqualTo(ISS_OBJECT_ID)
        assertThat(response.noradId).isEqualTo(NORAD_ISS)
        assertThat(response.status).isEqualTo(ObjectStatus.ACTIVE)
    }

    @Test
    fun `maps a sync report to a response`() {
        val report = SyncReport(updated = 3, failed = 1, decayed = 2)

        val response = report.toResponse()

        assertThat(response.updated).isEqualTo(3)
        assertThat(response.failed).isEqualTo(1)
        assertThat(response.decayed).isEqualTo(2)
    }
}
