package com.artemwhite.satellite.domain.models

import kotlin.math.asin
import kotlin.math.sqrt

data class GroundStation(
    val x: Double,
    val y: Double,
    val z: Double,
) {
    fun elevationDeg(satellite: Position): Double {
        val rx = satellite.x - x
        val ry = satellite.y - y
        val rz = satellite.z - z
        val range = sqrt(rx * rx + ry * ry + rz * rz)

        val observerRadius = sqrt(x * x + y * y + z * z)
        val upX = x / observerRadius
        val upY = y / observerRadius
        val upZ = z / observerRadius
        val sinElevation = (rx * upX + ry * upY + rz * upZ) / range
        return Math.toDegrees(asin(sinElevation.coerceIn(-1.0, 1.0)))
    }

    companion object {
        const val EARTH_RADIUS_KM: Double = 6378.137

        fun northPole(): GroundStation = GroundStation(0.0, 0.0, EARTH_RADIUS_KM)
    }
}
