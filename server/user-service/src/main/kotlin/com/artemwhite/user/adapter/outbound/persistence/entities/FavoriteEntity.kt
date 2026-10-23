package com.artemwhite.user.adapter.outbound.persistence.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import java.io.Serializable
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "favorites")
@IdClass(FavoriteEntityId::class)
class FavoriteEntity(
    @Id
    @Column(name = "user_id")
    var userId: UUID,

    @Id
    @Column(name = "object_id")
    var objectId: UUID,

    @Column(name = "added_at", nullable = false)
    var addedAt: Instant,
)

class FavoriteEntityId : Serializable {
    var userId: UUID? = null
    var objectId: UUID? = null

    constructor()

    constructor(userId: UUID, objectId: UUID) {
        this.userId = userId
        this.objectId = objectId
    }

    override fun equals(other: Any?): Boolean =
        other is FavoriteEntityId && other.userId == userId && other.objectId == objectId

    override fun hashCode(): Int = 31 * (userId?.hashCode() ?: 0) + (objectId?.hashCode() ?: 0)
}
