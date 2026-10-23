package com.artemwhite.catalog.adapter.inbound.web.dto

import com.artemwhite.shared.model.enums.ObjectStatus
import jakarta.validation.constraints.NotNull

data class UpdateStatusRequest(
    @field:NotNull val status: ObjectStatus,
)
