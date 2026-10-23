package com.artemwhite.shared.model

import com.artemwhite.shared.model.enums.EventType
import com.artemwhite.shared.model.enums.ObjectStatus
import java.time.Instant
import java.util.UUID

data class SatelliteEvent(
    val objectId: UUID,
    val status: ObjectStatus,
    val type: EventType,
    val occurredAt: Instant,
)
