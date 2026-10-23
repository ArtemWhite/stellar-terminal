package com.artemwhite.user.adapter.outbound.persistence.repositories

import com.artemwhite.user.adapter.outbound.persistence.entities.FavoriteEntity
import com.artemwhite.user.adapter.outbound.persistence.entities.FavoriteEntityId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

interface FavoriteJpaRepository : JpaRepository<FavoriteEntity, FavoriteEntityId> {
    fun findByUserIdOrderByAddedAtDesc(userId: UUID, pageable: Pageable): Page<FavoriteEntity>

    fun findByObjectId(objectId: UUID): List<FavoriteEntity>

    fun existsByUserIdAndObjectId(userId: UUID, objectId: UUID): Boolean

    @Transactional
    fun deleteByUserIdAndObjectId(userId: UUID, objectId: UUID): Long
}
