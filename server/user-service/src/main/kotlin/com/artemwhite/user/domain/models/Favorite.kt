package com.artemwhite.user.domain.models

import java.time.Instant
import java.util.UUID

class Favorite(
    val userId: UUID,
    val objectId: UUID,
    val addedAt: Instant,
) {
    companion object {
        fun create(userId: UUID, objectId: UUID, now: Instant): Favorite =
            Favorite(userId = userId, objectId = objectId, addedAt = now)
    }
}
