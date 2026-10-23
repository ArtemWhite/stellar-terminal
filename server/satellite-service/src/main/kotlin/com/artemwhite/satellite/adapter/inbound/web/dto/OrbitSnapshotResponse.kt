package com.artemwhite.satellite.adapter.inbound.web.dto

import java.time.Instant

data class OrbitSnapshotResponse(
    val position: PositionResponse,
    val calculatedAt: Instant,
)
