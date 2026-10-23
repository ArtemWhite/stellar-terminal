package com.artemwhite.catalog.adapter.outbound.persistence.mappers

import com.artemwhite.catalog.adapter.outbound.persistence.entities.SpaceObjectEntity
import com.artemwhite.catalog.testutil.SpaceObjectFixtures.NOW
import com.artemwhite.catalog.testutil.SpaceObjectFixtures.satellite
import com.artemwhite.shared.model.enums.ObjectCategory
import com.artemwhite.shared.model.enums.ObjectStatus
import com.artemwhite.shared.model.enums.SatelliteSubcategory
import com.artemwhite.shared.model.enums.SourceType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class SpaceObjectMapperTest {

    @Test
    fun `maps an entity to a domain object`() {
        val entity = SpaceObjectEntity(
            id = UUID.randomUUID(),
            name = "ISS",
            category = ObjectCategory.SATELLITE,
            subcategory = SatelliteSubcategory.SCIENTIFIC,
            description = "International Space Station",
            sourceId = "25544",
            sourceType = SourceType.CELESTRAK,
            status = ObjectStatus.ACTIVE,
            createdAt = NOW,
            updatedAt = NOW,
        )

        val domain = entity.toDomain()

        assertThat(domain.id).isEqualTo(entity.id)
        assertThat(domain.name).isEqualTo("ISS")
        assertThat(domain.category).isEqualTo(ObjectCategory.SATELLITE)
        assertThat(domain.subcategory).isEqualTo(SatelliteSubcategory.SCIENTIFIC)
        assertThat(domain.description).isEqualTo("International Space Station")
        assertThat(domain.sourceId).isEqualTo("25544")
        assertThat(domain.sourceType).isEqualTo(SourceType.CELESTRAK)
        assertThat(domain.status).isEqualTo(ObjectStatus.ACTIVE)
        assertThat(domain.createdAt).isEqualTo(NOW)
        assertThat(domain.updatedAt).isEqualTo(NOW)
    }

    @Test
    fun `maps a domain object to an entity`() {
        val domain = satellite()

        val entity = domain.toEntity()

        assertThat(entity.id).isEqualTo(domain.id)
        assertThat(entity.name).isEqualTo(domain.name)
        assertThat(entity.category).isEqualTo(domain.category)
        assertThat(entity.subcategory).isEqualTo(domain.subcategory)
        assertThat(entity.description).isEqualTo(domain.description)
        assertThat(entity.sourceId).isEqualTo(domain.sourceId)
        assertThat(entity.sourceType).isEqualTo(domain.sourceType)
        assertThat(entity.status).isEqualTo(domain.status)
        assertThat(entity.createdAt).isEqualTo(domain.createdAt)
        assertThat(entity.updatedAt).isEqualTo(domain.updatedAt)
    }
}
