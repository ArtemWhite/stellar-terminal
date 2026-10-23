package com.artemwhite.user.adapter.inbound.web.dto

import com.artemwhite.user.domain.models.Role
import java.time.Instant
import java.util.UUID

data class UserResponse(
    val id: UUID,
    val nickname: String,
    val email: String,
    val role: Role,
    val createdAt: Instant,
)
