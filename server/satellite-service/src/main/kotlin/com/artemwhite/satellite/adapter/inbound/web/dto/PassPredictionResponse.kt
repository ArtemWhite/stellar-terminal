package com.artemwhite.satellite.adapter.inbound.web.dto

import java.time.Instant
import java.util.UUID

data class PassPredictionResponse(
    val objectId: UUID,
    val start: Instant,
    val end: Instant,
    val maxElevation: Double,
)
