package com.artemwhite.user.adapter.outbound.persistence.entities

import com.artemwhite.user.domain.models.Role
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    val id: UUID,

    @Column(nullable = false)
    var nickname: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(name = "password_hash", nullable = false)
    var passwordHash: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role,

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant,
)
