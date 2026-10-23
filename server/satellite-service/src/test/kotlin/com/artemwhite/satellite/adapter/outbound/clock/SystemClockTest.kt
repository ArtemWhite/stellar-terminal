package com.artemwhite.satellite.adapter.outbound.clock

import java.time.Instant
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SystemClockTest {

    private val clock = SystemClock()

    @Test
    fun `now returns the current instant`() {
        val before = Instant.now()
        val now = clock.now()
        val after = Instant.now()

        assertThat(now).isBetween(before, after)
    }
}
