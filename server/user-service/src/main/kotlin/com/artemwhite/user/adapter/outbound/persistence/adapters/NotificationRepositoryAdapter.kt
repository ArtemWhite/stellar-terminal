package com.artemwhite.user.adapter.outbound.persistence.adapters

import com.artemwhite.user.adapter.outbound.persistence.mappers.toDomain
import com.artemwhite.user.adapter.outbound.persistence.mappers.toEntity
import com.artemwhite.user.adapter.outbound.persistence.repositories.NotificationJpaRepository
import com.artemwhite.user.domain.models.Notification
import com.artemwhite.user.domain.models.PageResult
import com.artemwhite.user.domain.ports.outbound.NotificationRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class NotificationRepositoryAdapter(
    private val jpaRepository: NotificationJpaRepository,
) : NotificationRepository {

    override fun findById(id: UUID): Notification? =
        jpaRepository.findById(id).map { it.toDomain() }.orElse(null)

    override fun findByUser(userId: UUID, page: Int, size: Int): PageResult<Notification> {
        val result = jpaRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(page, size))
        return PageResult(
            items = result.content.map { it.toDomain() },
            page = page,
            size = size,
            total = result.totalElements,
        )
    }

    override fun save(notification: Notification): Notification =
        jpaRepository.save(notification.toEntity()).toDomain()
}
