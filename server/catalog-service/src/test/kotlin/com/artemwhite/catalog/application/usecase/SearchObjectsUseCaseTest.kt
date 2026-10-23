package com.artemwhite.catalog.application.usecase

import com.artemwhite.catalog.domain.models.SearchFilter
import com.artemwhite.catalog.testutil.FakeSpaceObjectRepository
import com.artemwhite.catalog.testutil.SpaceObjectFixtures.satellite
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SearchObjectsUseCaseTest {

    private val repository = FakeSpaceObjectRepository()
    private val useCase = SearchObjectsUseCase(repository)

    @Test
    fun `delegates to the repository and returns the page`() {
        repository.save(satellite())
        repository.save(satellite())

        val result = useCase.execute(SearchFilter())

        assertThat(result.items).hasSize(2)
        assertThat(result.total).isEqualTo(2)
        assertThat(result.page).isEqualTo(0)
    }
}
