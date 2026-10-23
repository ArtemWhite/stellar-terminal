package com.artemwhite.user.adapter.outbound.clock

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant

class SystemClockTest {

    private val clock = SystemClock()

    @Test
    fun `returns the current wall-clock time`() {
        val before = Instant.now()
        val now = clock.now()
        val after = Instant.now()

        assertThat(now).isBetween(before, after)
    }
}
