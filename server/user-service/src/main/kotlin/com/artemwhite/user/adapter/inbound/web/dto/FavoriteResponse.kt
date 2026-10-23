package com.artemwhite.user.adapter.inbound.web.dto

import com.artemwhite.shared.model.enums.ObjectCategory
import java.time.Instant
import java.util.UUID

data class FavoriteResponse(
    val objectId: UUID,
    val name: String,
    val category: ObjectCategory,
    val addedAt: Instant,
)
