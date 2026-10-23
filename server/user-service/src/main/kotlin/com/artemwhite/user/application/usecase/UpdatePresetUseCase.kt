package com.artemwhite.user.application.usecase

import com.artemwhite.user.domain.exceptions.PresetNotFoundException
import com.artemwhite.user.domain.models.VisualizationPreset
import com.artemwhite.user.domain.ports.outbound.VisualizationPresetRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UpdatePresetUseCase(
    private val presetRepository: VisualizationPresetRepository,
) {
    fun execute(id: UUID, colors: String, scale: Double): VisualizationPreset {
        val preset = presetRepository.findById(id) ?: throw PresetNotFoundException(id)
        preset.update(colors, scale)
        return presetRepository.save(preset)
    }
}
