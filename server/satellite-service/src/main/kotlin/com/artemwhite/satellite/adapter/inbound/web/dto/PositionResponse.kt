package com.artemwhite.satellite.adapter.inbound.web.dto

data class PositionResponse(
    val x: Double,
    val y: Double,
    val z: Double,
    val vx: Double,
    val vy: Double,
    val vz: Double,
)
