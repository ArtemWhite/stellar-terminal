package com.artemwhite.user.adapter.outbound.persistence.repositories

import com.artemwhite.user.adapter.outbound.persistence.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserJpaRepository : JpaRepository<UserEntity, UUID> {
    fun findByEmail(email: String): UserEntity?

    fun existsByEmail(email: String): Boolean
}
