package com.artemwhite.user.adapter.inbound.web.dto

import jakarta.validation.constraints.NotBlank

data class CollectionRequest(
    @field:NotBlank val name: String,
)
