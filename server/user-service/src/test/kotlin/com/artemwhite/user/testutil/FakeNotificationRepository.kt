package com.artemwhite.user.testutil

import com.artemwhite.user.domain.models.Notification
import com.artemwhite.user.domain.models.PageResult
import com.artemwhite.user.domain.ports.outbound.NotificationRepository
import java.util.UUID

class FakeNotificationRepository : NotificationRepository {

    val store = LinkedHashMap<UUID, Notification>()

    override fun findById(id: UUID): Notification? = store[id]

    override fun findByUser(userId: UUID, page: Int, size: Int): PageResult<Notification> {
        val all = store.values.filter { it.userId == userId }.sortedByDescending { it.createdAt }
        val from = page * size
        return PageResult(
            items = all.drop(from).take(size),
            page = page,
            size = size,
            total = all.size.toLong(),
        )
    }

    override fun save(notification: Notification): Notification {
        store[notification.id] = notification
        return notification
    }
}
