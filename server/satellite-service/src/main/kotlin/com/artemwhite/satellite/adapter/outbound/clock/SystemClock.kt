package com.artemwhite.satellite.adapter.outbound.clock

import com.artemwhite.satellite.domain.ports.outbound.Clock
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class SystemClock : Clock {
    override fun now(): Instant = Instant.now()
}
