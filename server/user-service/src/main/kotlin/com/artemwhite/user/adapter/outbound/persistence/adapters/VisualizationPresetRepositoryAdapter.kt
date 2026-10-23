package com.artemwhite.user.adapter.outbound.persistence.adapters

import com.artemwhite.user.adapter.outbound.persistence.mappers.toDomain
import com.artemwhite.user.adapter.outbound.persistence.mappers.toEntity
import com.artemwhite.user.adapter.outbound.persistence.repositories.VisualizationPresetJpaRepository
import com.artemwhite.user.domain.models.PageResult
import com.artemwhite.user.domain.models.VisualizationPreset
import com.artemwhite.user.domain.ports.outbound.VisualizationPresetRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class VisualizationPresetRepositoryAdapter(
    private val jpaRepository: VisualizationPresetJpaRepository,
) : VisualizationPresetRepository {

    override fun findById(id: UUID): VisualizationPreset? =
        jpaRepository.findById(id).map { it.toDomain() }.orElse(null)

    override fun findByUser(userId: UUID, page: Int, size: Int): PageResult<VisualizationPreset> {
        val result = jpaRepository.findByUserIdOrderByIdAsc(userId, PageRequest.of(page, size))
        return PageResult(
            items = result.content.map { it.toDomain() },
            page = page,
            size = size,
            total = result.totalElements,
        )
    }

    override fun save(preset: VisualizationPreset): VisualizationPreset =
        jpaRepository.save(preset.toEntity()).toDomain()
}
