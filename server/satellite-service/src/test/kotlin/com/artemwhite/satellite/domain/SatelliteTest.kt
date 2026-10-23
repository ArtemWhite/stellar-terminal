package com.artemwhite.satellite.domain

import com.artemwhite.satellite.domain.models.Satellite
import com.artemwhite.satellite.testutil.SatelliteFixtures.satellite
import com.artemwhite.shared.model.enums.ObjectStatus
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class SatelliteTest {

    @Test
    fun `factory create produces an ACTIVE satellite`() {
        val id = UUID.randomUUID()

        val created = Satellite.create(id, 25544)

        assertThat(created.objectId).isEqualTo(id)
        assertThat(created.noradId).isEqualTo(25544)
        assertThat(created.status).isEqualTo(ObjectStatus.ACTIVE)
    }

    @Test
    fun `factory create rejects a non-positive norad id`() {
        assertThatThrownBy { Satellite.create(UUID.randomUUID(), 0) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `constructor rejects a non-positive norad id`() {
        assertThatThrownBy { Satellite(UUID.randomUUID(), -1, ObjectStatus.ACTIVE) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `changeStatus updates the status`() {
        val s = satellite()

        s.changeStatus(ObjectStatus.DECAYED)

        assertThat(s.status).isEqualTo(ObjectStatus.DECAYED)
    }
}
