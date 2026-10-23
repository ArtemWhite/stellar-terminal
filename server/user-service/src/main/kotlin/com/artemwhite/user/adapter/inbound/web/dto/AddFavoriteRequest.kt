package com.artemwhite.user.adapter.inbound.web.dto

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class AddFavoriteRequest(
    @field:NotNull val objectId: UUID,
)
