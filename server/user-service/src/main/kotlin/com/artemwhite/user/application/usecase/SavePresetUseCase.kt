package com.artemwhite.user.application.usecase

import com.artemwhite.user.application.command.SavePresetCommand
import com.artemwhite.user.domain.models.VisualizationPreset
import com.artemwhite.user.domain.ports.outbound.VisualizationPresetRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SavePresetUseCase(
    private val presetRepository: VisualizationPresetRepository,
) {
    fun execute(command: SavePresetCommand): VisualizationPreset =
        presetRepository.save(
            VisualizationPreset.create(UUID.randomUUID(), command.userId, command.colors, command.scale),
        )
}
