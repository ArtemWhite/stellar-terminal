package com.artemwhite.satellite.adapter.inbound.web.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.util.UUID

data class RegisterSatelliteRequest(
    @field:NotNull val objectId: UUID,
    @field:Positive val noradId: Int,
)
