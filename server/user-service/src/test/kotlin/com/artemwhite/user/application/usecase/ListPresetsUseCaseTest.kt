package com.artemwhite.user.application.usecase

import com.artemwhite.user.testutil.FakeVisualizationPresetRepository
import com.artemwhite.user.testutil.UserFixtures.preset
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class ListPresetsUseCaseTest {

    private val repository = FakeVisualizationPresetRepository()
    private val useCase = ListPresetsUseCase(repository)

    @Test
    fun `returns the presets of a user as a page`() {
        val userId = UUID.randomUUID()
        repository.save(preset(userId = userId))
        repository.save(preset(userId = UUID.randomUUID()))

        val result = useCase.execute(userId, 0, 20)

        assertThat(result.items).hasSize(1)
        assertThat(result.total).isEqualTo(1)
    }

    @Test
    fun `paginates presets`() {
        val userId = UUID.randomUUID()
        repeat(3) { repository.save(preset(userId = userId)) }

        val first = useCase.execute(userId, 0, 2)
        val second = useCase.execute(userId, 1, 2)

        assertThat(first.items).hasSize(2)
        assertThat(second.items).hasSize(1)
        assertThat(first.total).isEqualTo(3)
    }
}
