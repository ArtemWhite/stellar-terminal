package com.artemwhite.user.adapter.inbound.web.dto

import java.util.UUID

data class CollectionResponse(
    val id: UUID,
    val name: String,
    val objectIds: List<UUID>,
)
