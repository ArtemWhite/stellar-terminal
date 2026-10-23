package com.artemwhite.catalog.application.usecase

import com.artemwhite.catalog.application.command.CreateObjectCommand
import com.artemwhite.catalog.domain.exceptions.InvalidCategoryException
import com.artemwhite.catalog.domain.ports.outbound.Clock
import com.artemwhite.catalog.testutil.FakeSpaceObjectRepository
import com.artemwhite.catalog.testutil.SpaceObjectFixtures.NOW
import com.artemwhite.shared.model.enums.ObjectCategory
import com.artemwhite.shared.model.enums.ObjectStatus
import com.artemwhite.shared.model.enums.SatelliteSubcategory
import com.artemwhite.shared.model.enums.SourceType
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class CreateObjectUseCaseTest {

    private val repository = FakeSpaceObjectRepository()
    private val clock = Clock { NOW }
    private val useCase = CreateObjectUseCase(repository, clock)

    @Test
    fun `creates an ACTIVE object and persists it`() {
        val command = CreateObjectCommand(
            name = "ISS",
            category = ObjectCategory.SATELLITE,
            subcategory = null,
            description = "International Space Station",
            sourceId = "25544",
            sourceType = SourceType.CELESTRAK,
        )

        val created = useCase.execute(command)

        assertThat(created.status).isEqualTo(ObjectStatus.ACTIVE)
        assertThat(created.name).isEqualTo("ISS")
        assertThat(created.createdAt).isEqualTo(created.updatedAt)
        assertThat(repository.findById(created.id)).isSameAs(created)
    }

    @Test
    fun `rejects a subcategory on a non-satellite category`() {
        val command = CreateObjectCommand(
            name = "Earth",
            category = ObjectCategory.PLANET,
            subcategory = SatelliteSubcategory.WEATHER,
            description = "Planet Earth",
            sourceId = "src-1",
            sourceType = SourceType.MANUAL,
        )

        assertThatThrownBy { useCase.execute(command) }
            .isInstanceOf(InvalidCategoryException::class.java)
    }
}
