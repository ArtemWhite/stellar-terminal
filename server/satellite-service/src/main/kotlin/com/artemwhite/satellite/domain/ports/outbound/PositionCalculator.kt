package com.artemwhite.satellite.domain.ports.outbound

import com.artemwhite.satellite.domain.models.Position
import com.artemwhite.satellite.domain.models.TleRecord
import java.time.Instant

fun interface PositionCalculator {
    fun propagate(tle: TleRecord, at: Instant): Position
}
