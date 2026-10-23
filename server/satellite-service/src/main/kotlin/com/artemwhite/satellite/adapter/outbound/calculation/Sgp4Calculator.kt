package com.artemwhite.satellite.adapter.outbound.calculation

import com.artemwhite.satellite.domain.models.Position
import com.artemwhite.satellite.domain.models.TleRecord
import com.artemwhite.satellite.domain.ports.outbound.PositionCalculator
import org.springframework.stereotype.Component
import java.time.Instant
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Component
class Sgp4Calculator(
    private val parser: TleParser,
) : PositionCalculator {

    override fun propagate(tle: TleRecord, at: Instant): Position {
        val e = parser.parseElements(tle.line1, tle.line2)

        val periodSeconds = SECONDS_PER_DAY / e.meanMotionRevPerDay
        val meanMotionRadPerSec = TWO_PI / periodSeconds
        val semiMajorAxis = Math.cbrt(MU / (meanMotionRadPerSec * meanMotionRadPerSec))

        val elapsed = (at.epochSecond - e.epoch.epochSecond) + (at.nano - e.epoch.nano) / 1_000_000_000.0
        val argumentOfLatitude = e.argPerigeeRad + e.meanAnomalyRad + meanMotionRadPerSec * elapsed
        val speed = sqrt(MU / semiMajorAxis)

        val cosI = cos(e.inclinationRad)
        val sinI = sin(e.inclinationRad)
        val cosOmega = cos(e.raanRad)
        val sinOmega = sin(e.raanRad)
        val cosU = cos(argumentOfLatitude)
        val sinU = sin(argumentOfLatitude)

        val x = semiMajorAxis * (cosOmega * cosU - sinOmega * sinU * cosI)
        val y = semiMajorAxis * (sinOmega * cosU + cosOmega * sinU * cosI)
        val z = semiMajorAxis * (sinU * sinI)

        val vx = -speed * (cosOmega * sinU + sinOmega * cosU * cosI)
        val vy = -speed * (sinOmega * sinU - cosOmega * cosU * cosI)
        val vz = speed * (cosU * sinI)

        return Position(x, y, z, vx, vy, vz)
    }

    companion object {
        private const val MU = 398_600.4418
        private const val SECONDS_PER_DAY = 86_400.0
        private const val TWO_PI = 2.0 * Math.PI
    }
}
