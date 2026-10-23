package com.artemwhite.satellite.domain.ports.outbound

import com.artemwhite.shared.model.SatelliteEvent

fun interface EventPublisher {
    fun publish(event: SatelliteEvent)
}
