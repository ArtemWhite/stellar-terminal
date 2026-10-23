package com.artemwhite.user.domain.ports.outbound

import com.artemwhite.user.domain.models.PageResult
import com.artemwhite.user.domain.models.VisualizationPreset
import java.util.UUID

interface VisualizationPresetRepository {
    fun findById(id: UUID): VisualizationPreset?

    fun findByUser(userId: UUID, page: Int, size: Int): PageResult<VisualizationPreset>

    fun save(preset: VisualizationPreset): VisualizationPreset
}
