package com.artemwhite.user.application.usecase

import com.artemwhite.user.domain.models.PageResult
import com.artemwhite.user.domain.models.VisualizationPreset
import com.artemwhite.user.domain.ports.outbound.VisualizationPresetRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ListPresetsUseCase(
    private val presetRepository: VisualizationPresetRepository,
) {
    fun execute(userId: UUID, page: Int, size: Int): PageResult<VisualizationPreset> =
        presetRepository.findByUser(userId, page, size)
}
