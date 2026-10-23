package com.artemwhite.user.adapter.inbound.web.dto

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class CollectionObjectRequest(
    @field:NotNull val objectId: UUID,
)
