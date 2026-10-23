package com.artemwhite.user.domain.ports.outbound

import com.artemwhite.user.domain.models.Notification
import com.artemwhite.user.domain.models.PageResult
import java.util.UUID

interface NotificationRepository {
    fun findById(id: UUID): Notification?

    fun findByUser(userId: UUID, page: Int, size: Int): PageResult<Notification>

    fun save(notification: Notification): Notification
}
