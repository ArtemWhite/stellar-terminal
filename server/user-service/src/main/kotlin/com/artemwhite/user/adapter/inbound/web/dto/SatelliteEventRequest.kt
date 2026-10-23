package com.artemwhite.user.adapter.inbound.web.dto

import com.artemwhite.shared.model.enums.EventType
import com.artemwhite.shared.model.enums.ObjectStatus
import jakarta.validation.constraints.NotNull
import java.time.Instant
import java.util.UUID

data class SatelliteEventRequest(
    @field:NotNull val objectId: UUID,
    @field:NotNull val status: ObjectStatus,
    @field:NotNull val type: EventType,
    @field:NotNull val occurredAt: Instant,
)
