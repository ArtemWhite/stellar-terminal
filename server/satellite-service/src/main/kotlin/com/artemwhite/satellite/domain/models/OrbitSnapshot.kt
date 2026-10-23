package com.artemwhite.satellite.domain.models

import java.time.Instant

data class OrbitSnapshot(
    val position: Position,
    val calculatedAt: Instant,
)
