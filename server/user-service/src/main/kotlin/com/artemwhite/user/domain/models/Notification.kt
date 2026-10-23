package com.artemwhite.user.domain.models

import java.time.Instant
import java.util.UUID

class Notification(
    val id: UUID,
    val userId: UUID,
    val text: String,
    read: Boolean,
    val createdAt: Instant,
) {
    var read: Boolean = read
        private set

    init {
        require(text.isNotBlank()) { "notification text must not be blank" }
    }

    fun markAsRead() {
        read = true
    }

    companion object {
        fun create(id: UUID, userId: UUID, text: String, now: Instant): Notification =
            Notification(id = id, userId = userId, text = text, read = false, createdAt = now)
    }
}
