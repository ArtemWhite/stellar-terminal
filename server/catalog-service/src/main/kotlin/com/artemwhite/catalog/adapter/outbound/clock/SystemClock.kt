package com.artemwhite.catalog.adapter.outbound.clock

import com.artemwhite.catalog.domain.ports.outbound.Clock
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class SystemClock : Clock {
    override fun now(): Instant = Instant.now()
}
