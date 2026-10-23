package com.artemwhite.user.testutil

import com.artemwhite.user.domain.models.Email
import com.artemwhite.user.domain.models.User
import com.artemwhite.user.domain.ports.outbound.UserRepository
import java.util.UUID

class FakeUserRepository : UserRepository {

    val store = LinkedHashMap<UUID, User>()

    override fun findById(id: UUID): User? = store[id]

    override fun findByEmail(email: Email): User? = store.values.firstOrNull { it.email == email }

    override fun existsByEmail(email: Email): Boolean = store.values.any { it.email == email }

    override fun save(user: User): User {
        store[user.id] = user
        return user
    }
}
