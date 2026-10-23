package com.artemwhite.user.adapter.outbound.persistence.repositories

import com.artemwhite.user.adapter.outbound.persistence.entities.NotificationEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface NotificationJpaRepository : JpaRepository<NotificationEntity, UUID> {
    fun findByUserIdOrderByCreatedAtDesc(userId: UUID, pageable: Pageable): Page<NotificationEntity>
}
