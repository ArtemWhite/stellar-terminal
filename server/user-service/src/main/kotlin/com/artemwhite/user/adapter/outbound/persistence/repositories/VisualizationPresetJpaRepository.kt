package com.artemwhite.user.adapter.outbound.persistence.repositories

import com.artemwhite.user.adapter.outbound.persistence.entities.VisualizationPresetEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface VisualizationPresetJpaRepository : JpaRepository<VisualizationPresetEntity, UUID> {
    fun findByUserIdOrderByIdAsc(userId: UUID, pageable: Pageable): Page<VisualizationPresetEntity>
}
