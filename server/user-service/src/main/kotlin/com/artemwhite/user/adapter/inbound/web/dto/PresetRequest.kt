package com.artemwhite.user.adapter.inbound.web.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class PresetRequest(
    @field:NotBlank val colors: String,
    @field:NotNull @field:Positive val scale: Double,
)
