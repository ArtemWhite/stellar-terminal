package com.artemwhite.user.testutil

import com.artemwhite.user.domain.models.PageResult
import com.artemwhite.user.domain.models.VisualizationPreset
import com.artemwhite.user.domain.ports.outbound.VisualizationPresetRepository
import java.util.UUID

class FakeVisualizationPresetRepository : VisualizationPresetRepository {

    val store = LinkedHashMap<UUID, VisualizationPreset>()

    override fun findById(id: UUID): VisualizationPreset? = store[id]

    override fun findByUser(userId: UUID, page: Int, size: Int): PageResult<VisualizationPreset> {
        val all = store.values.filter { it.userId == userId }.sortedBy { it.id }
        val from = page * size
        return PageResult(
            items = all.drop(from).take(size),
            page = page,
            size = size,
            total = all.size.toLong(),
        )
    }

    override fun save(preset: VisualizationPreset): VisualizationPreset {
        store[preset.id] = preset
        return preset
    }
}
