package com.artemwhite.satellite.adapter.outbound.calculation

import com.artemwhite.satellite.domain.models.Position
import com.artemwhite.satellite.testutil.SatelliteFixtures.ISS_TLE
import kotlin.math.PI
import kotlin.math.sqrt
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test

class Sgp4CalculatorTest {

    private val calculator = Sgp4Calculator(TleParser())

    @Test
    fun `returns to the same position after one full period`() {
        val periodSeconds = 86_400.0 / 15.5
        val p1 = calculator.propagate(ISS_TLE, ISS_TLE.epoch)
        val p2 = calculator.propagate(ISS_TLE, ISS_TLE.epoch.plusNanos((periodSeconds * 1e9).toLong()))

        assertThat(p2.x).isCloseTo(p1.x, within(1e-3))
        assertThat(p2.y).isCloseTo(p1.y, within(1e-3))
        assertThat(p2.z).isCloseTo(p1.z, within(1e-3))
    }

    @Test
    fun `keeps the radius constant over time`() {
        val p1 = calculator.propagate(ISS_TLE, ISS_TLE.epoch)
        val p2 = calculator.propagate(ISS_TLE, ISS_TLE.epoch.plusSeconds(3_600))

        assertThat(radius(p2)).isCloseTo(radius(p1), within(1e-6))
    }

    @Test
    fun `keeps velocity perpendicular to position`() {
        val p = calculator.propagate(ISS_TLE, ISS_TLE.epoch.plusSeconds(1_234))

        val dot = p.x * p.vx + p.y * p.vy + p.z * p.vz

        assertThat(dot).isCloseTo(0.0, within(1e-6))
    }

    @Test
    fun `radius matches the semi-major axis derived from the mean motion`() {
        val periodSeconds = 86_400.0 / 15.5
        val meanMotionRadPerSec = 2.0 * PI / periodSeconds
        val semiMajorAxis = Math.cbrt(398_600.4418 / (meanMotionRadPerSec * meanMotionRadPerSec))

        val p = calculator.propagate(ISS_TLE, ISS_TLE.epoch)

        assertThat(radius(p)).isCloseTo(semiMajorAxis, within(1e-6))
    }

    private fun radius(p: Position): Double = sqrt(p.x * p.x + p.y * p.y + p.z * p.z)
}
