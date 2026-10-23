package com.artemwhite.user.adapter.inbound.web.dto

import java.util.UUID

data class PresetResponse(
    val id: UUID,
    val colors: String,
    val scale: Double,
)
