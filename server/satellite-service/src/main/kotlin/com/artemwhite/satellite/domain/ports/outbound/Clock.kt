package com.artemwhite.satellite.domain.ports.outbound

import java.time.Instant

fun interface Clock {
    fun now(): Instant
}
