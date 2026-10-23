package com.artemwhite.user.adapter.outbound.persistence.repositories

import com.artemwhite.user.adapter.outbound.persistence.entities.CollectionEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CollectionJpaRepository : JpaRepository<CollectionEntity, UUID> {
    fun findByUserIdOrderByNameAsc(userId: UUID, pageable: Pageable): Page<CollectionEntity>
}
