package com.artemwhite.user.application.usecase

import com.artemwhite.user.domain.exceptions.PresetNotFoundException
import com.artemwhite.user.testutil.FakeVisualizationPresetRepository
import com.artemwhite.user.testutil.UserFixtures.preset
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.UUID

class UpdatePresetUseCaseTest {

    private val repository = FakeVisualizationPresetRepository()
    private val useCase = UpdatePresetUseCase(repository)

    @Test
    fun `updates the preset`() {
        val p = preset(userId = UUID.randomUUID())
        repository.save(p)

        val updated = useCase.execute(p.id, "#0000ff", 4.0)

        assertThat(updated.colors).isEqualTo("#0000ff")
        assertThat(updated.scale).isEqualTo(4.0)
    }

    @Test
    fun `throws when the preset does not exist`() {
        assertThatThrownBy { useCase.execute(UUID.randomUUID(), "#fff", 1.0) }
            .isInstanceOf(PresetNotFoundException::class.java)
    }
}
