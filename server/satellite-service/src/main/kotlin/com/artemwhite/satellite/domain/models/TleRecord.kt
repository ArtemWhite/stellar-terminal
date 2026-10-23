package com.artemwhite.satellite.domain.models

import java.time.Instant

data class TleRecord(
    val line1: String,
    val line2: String,
    val epoch: Instant,
    val fetchedAt: Instant,
)
