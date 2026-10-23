package com.artemwhite.user.adapter.inbound.web.dto

import java.time.Instant
import java.util.UUID

data class NotificationResponse(
    val id: UUID,
    val text: String,
    val read: Boolean,
    val createdAt: Instant,
)
