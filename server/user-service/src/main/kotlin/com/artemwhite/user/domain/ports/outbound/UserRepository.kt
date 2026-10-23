package com.artemwhite.user.domain.ports.outbound

import com.artemwhite.user.domain.models.Email
import com.artemwhite.user.domain.models.User
import java.util.UUID

interface UserRepository {
    fun findById(id: UUID): User?

    fun findByEmail(email: Email): User?

    fun existsByEmail(email: Email): Boolean

    fun save(user: User): User
}
