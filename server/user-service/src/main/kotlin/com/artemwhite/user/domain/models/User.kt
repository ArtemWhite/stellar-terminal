package com.artemwhite.user.domain.models

import java.time.Instant
import java.util.UUID

class User(
    val id: UUID,
    val nickname: String,
    val email: Email,
    val passwordHash: PasswordHash,
    role: Role,
    val createdAt: Instant,
) {
    var role: Role = role
        private set

    init {
        require(nickname.isNotBlank()) { "nickname must not be blank" }
    }

    fun changeRole(newRole: Role) {
        role = newRole
    }

    companion object {
        fun create(
            id: UUID,
            nickname: String,
            email: Email,
            passwordHash: PasswordHash,
            role: Role,
            now: Instant,
        ): User = User(
            id = id,
            nickname = nickname,
            email = email,
            passwordHash = passwordHash,
            role = role,
            createdAt = now,
        )
    }
}
