package com.artemwhite.satellite.adapter.outbound.persistence.mappers

import com.artemwhite.satellite.adapter.outbound.persistence.entities.OrbitSnapshotEntity
import com.artemwhite.satellite.adapter.outbound.persistence.entities.SatelliteEntity
import com.artemwhite.satellite.adapter.outbound.persistence.entities.TleRecordEntity
import com.artemwhite.satellite.domain.models.OrbitSnapshot
import com.artemwhite.satellite.testutil.SatelliteFixtures.ISS_OBJECT_ID
import com.artemwhite.satellite.testutil.SatelliteFixtures.ISS_TLE
import com.artemwhite.satellite.testutil.SatelliteFixtures.NOW
import com.artemwhite.satellite.testutil.SatelliteFixtures.SAMPLE_POSITION
import com.artemwhite.satellite.testutil.SatelliteFixtures.satellite
import com.artemwhite.shared.model.enums.ObjectStatus
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SatellitePersistenceMapperTest {

    @Test
    fun `maps a satellite entity to a domain object`() {
        val entity = SatelliteEntity(ISS_OBJECT_ID, 25544, ObjectStatus.DECAYED)

        val domain = entity.toDomain()

        assertThat(domain.objectId).isEqualTo(ISS_OBJECT_ID)
        assertThat(domain.noradId).isEqualTo(25544)
        assertThat(domain.status).isEqualTo(ObjectStatus.DECAYED)
    }

    @Test
    fun `maps a satellite domain object to an entity`() {
        val domain = satellite()

        val entity = domain.toEntity()

        assertThat(entity.objectId).isEqualTo(domain.objectId)
        assertThat(entity.noradId).isEqualTo(domain.noradId)
        assertThat(entity.status).isEqualTo(domain.status)
    }

    @Test
    fun `maps a TLE entity to a domain record`() {
        val entity = TleRecordEntity(ISS_OBJECT_ID, ISS_TLE.line1, ISS_TLE.line2, ISS_TLE.epoch, ISS_TLE.fetchedAt)

        val domain = entity.toDomain()

        assertThat(domain).isEqualTo(ISS_TLE)
    }

    @Test
    fun `maps a TLE domain record to an entity`() {
        val entity = ISS_TLE.toEntity(ISS_OBJECT_ID)

        assertThat(entity.objectId).isEqualTo(ISS_OBJECT_ID)
        assertThat(entity.line1).isEqualTo(ISS_TLE.line1)
        assertThat(entity.line2).isEqualTo(ISS_TLE.line2)
        assertThat(entity.epoch).isEqualTo(ISS_TLE.epoch)
        assertThat(entity.fetchedAt).isEqualTo(ISS_TLE.fetchedAt)
    }

    @Test
    fun `maps an orbit snapshot entity to a domain object`() {
        val entity = OrbitSnapshotEntity(
            UUID.randomUUID(),
            ISS_OBJECT_ID,
            SAMPLE_POSITION.x,
            SAMPLE_POSITION.y,
            SAMPLE_POSITION.z,
            SAMPLE_POSITION.vx,
            SAMPLE_POSITION.vy,
            SAMPLE_POSITION.vz,
            NOW,
        )

        val domain = entity.toDomain()

        assertThat(domain.position).isEqualTo(SAMPLE_POSITION)
        assertThat(domain.calculatedAt).isEqualTo(NOW)
    }

    @Test
    fun `maps an orbit snapshot domain object to an entity`() {
        val domain = OrbitSnapshot(SAMPLE_POSITION, NOW)

        val entity = domain.toEntity(ISS_OBJECT_ID)

        assertThat(entity.id).isNotNull
        assertThat(entity.objectId).isEqualTo(ISS_OBJECT_ID)
        assertThat(entity.x).isEqualTo(SAMPLE_POSITION.x)
        assertThat(entity.y).isEqualTo(SAMPLE_POSITION.y)
        assertThat(entity.z).isEqualTo(SAMPLE_POSITION.z)
        assertThat(entity.vx).isEqualTo(SAMPLE_POSITION.vx)
        assertThat(entity.vy).isEqualTo(SAMPLE_POSITION.vy)
        assertThat(entity.vz).isEqualTo(SAMPLE_POSITION.vz)
        assertThat(entity.calculatedAt).isEqualTo(NOW)
    }
}
