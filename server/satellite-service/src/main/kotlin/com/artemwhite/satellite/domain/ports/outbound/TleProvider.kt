package com.artemwhite.satellite.domain.ports.outbound

import com.artemwhite.satellite.domain.models.ActiveTle

fun interface TleProvider {
    fun fetchActiveSatellites(): List<ActiveTle>
}
