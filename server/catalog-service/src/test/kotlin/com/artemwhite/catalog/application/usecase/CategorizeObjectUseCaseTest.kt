package com.artemwhite.catalog.application.usecase

import com.artemwhite.catalog.domain.exceptions.InvalidCategoryException
import com.artemwhite.catalog.domain.exceptions.ObjectNotFoundException
import com.artemwhite.catalog.domain.ports.outbound.Clock
import com.artemwhite.catalog.testutil.FakeSpaceObjectRepository
import com.artemwhite.catalog.testutil.SpaceObjectFixtures.LATER
import com.artemwhite.catalog.testutil.SpaceObjectFixtures.satellite
import com.artemwhite.shared.model.enums.ObjectCategory
import com.artemwhite.shared.model.enums.SatelliteSubcategory
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.UUID

class CategorizeObjectUseCaseTest {

    private val repository = FakeSpaceObjectRepository()
    private val clock = Clock { LATER }
    private val useCase = CategorizeObjectUseCase(repository, clock)

    @Test
    fun `recategorizes an existing object`() {
        val obj = satellite()
        repository.save(obj)

        val result = useCase.execute(obj.id, ObjectCategory.STAR, null)

        assertThat(result.category).isEqualTo(ObjectCategory.STAR)
        assertThat(result.subcategory).isNull()
        assertThat(result.updatedAt).isEqualTo(LATER)
    }

    @Test
    fun `throws when the object does not exist`() {
        assertThatThrownBy {
            useCase.execute(UUID.randomUUID(), ObjectCategory.STAR, null)
        }
            .isInstanceOf(ObjectNotFoundException::class.java)
    }

    @Test
    fun `throws when a non-satellite gets a subcategory`() {
        val obj = satellite()
        repository.save(obj)

        assertThatThrownBy {
            useCase.execute(obj.id, ObjectCategory.PLANET, SatelliteSubcategory.WEATHER)
        }
            .isInstanceOf(InvalidCategoryException::class.java)
    }
}
