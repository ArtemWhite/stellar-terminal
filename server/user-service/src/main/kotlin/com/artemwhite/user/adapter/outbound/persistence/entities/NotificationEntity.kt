package com.artemwhite.user.adapter.outbound.persistence.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "notifications")
class NotificationEntity(
    @Id
    val id: UUID,

    @Column(name = "user_id", nullable = false)
    var userId: UUID,

    @Column(nullable = false)
    var text: String,

    @Column(name = "read", nullable = false)
    var read: Boolean,

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant,
)
