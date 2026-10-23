package com.artemwhite.catalog.domain

import com.artemwhite.catalog.domain.exceptions.InvalidCategoryException
import com.artemwhite.catalog.domain.models.SpaceObject
import com.artemwhite.catalog.testutil.SpaceObjectFixtures.LATER
import com.artemwhite.catalog.testutil.SpaceObjectFixtures.NOW
import com.artemwhite.catalog.testutil.SpaceObjectFixtures.satellite
import com.artemwhite.shared.model.enums.ObjectCategory
import com.artemwhite.shared.model.enums.ObjectStatus
import com.artemwhite.shared.model.enums.SatelliteSubcategory
import com.artemwhite.shared.model.enums.SourceType
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class SpaceObjectTest {

    @Test
    fun `changeStatus updates status and updatedAt`() {
        val obj = satellite()

        obj.changeStatus(ObjectStatus.DECAYED, LATER)

        assertThat(obj.status).isEqualTo(ObjectStatus.DECAYED)
        assertThat(obj.updatedAt).isEqualTo(LATER)
    }

    @Test
    fun `recategorize keeps subcategory for a satellite`() {
        val obj = satellite()

        obj.recategorize(ObjectCategory.SATELLITE, SatelliteSubcategory.MILITARY, LATER)

        assertThat(obj.category).isEqualTo(ObjectCategory.SATELLITE)
        assertThat(obj.subcategory).isEqualTo(SatelliteSubcategory.MILITARY)
        assertThat(obj.updatedAt).isEqualTo(LATER)
    }

    @Test
    fun `recategorize clears subcategory for a non-satellite`() {
        val obj = satellite()

        obj.recategorize(ObjectCategory.STAR, null, LATER)

        assertThat(obj.category).isEqualTo(ObjectCategory.STAR)
        assertThat(obj.subcategory).isNull()
    }

    @Test
    fun `recategorize to non-satellite with subcategory throws`() {
        val obj = satellite()

        assertThatThrownBy {
            obj.recategorize(ObjectCategory.PLANET, SatelliteSubcategory.WEATHER, LATER)
        }
            .isInstanceOf(InvalidCategoryException::class.java)
    }

    @Test
    fun `factory create produces an ACTIVE object with matching timestamps`() {
        val obj = SpaceObject.create(
            id = UUID.randomUUID(),
            name = "ISS",
            category = ObjectCategory.SATELLITE,
            subcategory = SatelliteSubcategory.SCIENTIFIC,
            description = "International Space Station",
            sourceId = "25544",
            sourceType = SourceType.CELESTRAK,
            now = NOW,
        )

        assertThat(obj.status).isEqualTo(ObjectStatus.ACTIVE)
        assertThat(obj.createdAt).isEqualTo(NOW)
        assertThat(obj.updatedAt).isEqualTo(NOW)
    }

    @Test
    fun `constructor rejects a subcategory on a non-satellite category`() {
        assertThatThrownBy {
            SpaceObject(
                id = UUID.randomUUID(),
                name = "Earth",
                category = ObjectCategory.PLANET,
                subcategory = SatelliteSubcategory.WEATHER,
                description = "Planet Earth",
                sourceId = "src-1",
                sourceType = SourceType.MANUAL,
                status = ObjectStatus.ACTIVE,
                createdAt = NOW,
                updatedAt = NOW,
            )
        }
            .isInstanceOf(InvalidCategoryException::class.java)
    }
}
