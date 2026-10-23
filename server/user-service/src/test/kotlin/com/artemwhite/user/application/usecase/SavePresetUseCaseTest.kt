package com.artemwhite.user.application.usecase

import com.artemwhite.user.application.command.SavePresetCommand
import com.artemwhite.user.testutil.FakeVisualizationPresetRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class SavePresetUseCaseTest {

    private val repository = FakeVisualizationPresetRepository()
    private val useCase = SavePresetUseCase(repository)

    @Test
    fun `saves a preset`() {
        val userId = UUID.randomUUID()

        val saved = useCase.execute(SavePresetCommand(userId, "#00ff00", 2.0))

        assertThat(saved.userId).isEqualTo(userId)
        assertThat(saved.colors).isEqualTo("#00ff00")
        assertThat(saved.scale).isEqualTo(2.0)
        assertThat(repository.findById(saved.id)).isSameAs(saved)
    }
}
