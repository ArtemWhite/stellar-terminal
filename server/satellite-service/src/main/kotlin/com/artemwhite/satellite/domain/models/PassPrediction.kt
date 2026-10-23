package com.artemwhite.satellite.domain.models

import java.time.Instant
import java.util.UUID

data class PassPrediction(
    val objectId: UUID,
    val start: Instant,
    val end: Instant,
    val maxElevation: Double,
)
