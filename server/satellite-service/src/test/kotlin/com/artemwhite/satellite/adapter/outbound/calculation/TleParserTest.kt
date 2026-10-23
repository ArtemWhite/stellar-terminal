package com.artemwhite.satellite.adapter.outbound.calculation

import com.artemwhite.satellite.domain.exceptions.InvalidTleException
import com.artemwhite.satellite.testutil.SatelliteFixtures.NORAD_ISS
import com.artemwhite.satellite.testutil.SatelliteFixtures.NOW
import com.artemwhite.satellite.testutil.SatelliteFixtures.line1
import com.artemwhite.satellite.testutil.SatelliteFixtures.line2
import java.time.Instant
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test

class TleParserTest {

    private val parser = TleParser()

    @Test
    fun `parses every orbital element from the fixed columns`() {
        val elements = parser.parseElements(line1(), line2())

        assertThat(elements.noradId).isEqualTo(NORAD_ISS)
        assertThat(elements.epoch).isEqualTo(Instant.parse("2024-04-09T00:00:00Z"))
        assertThat(elements.inclinationRad).isCloseTo(Math.toRadians(51.6416), within(1e-9))
        assertThat(elements.raanRad).isCloseTo(Math.toRadians(35.1234), within(1e-9))
        assertThat(elements.argPerigeeRad).isCloseTo(Math.toRadians(45.6789), within(1e-9))
        assertThat(elements.meanAnomalyRad).isCloseTo(Math.toRadians(314.3210), within(1e-9))
        assertThat(elements.meanMotionRevPerDay).isCloseTo(15.5, within(1e-9))
    }

    @Test
    fun `wraps the elements into an ActiveTle`() {
        val active = parser.toActiveTle(line1(), line2(), NOW)

        assertThat(active.noradId).isEqualTo(NORAD_ISS)
        assertThat(active.tle.line1).isEqualTo(line1())
        assertThat(active.tle.line2).isEqualTo(line2())
        assertThat(active.tle.epoch).isEqualTo(Instant.parse("2024-04-09T00:00:00Z"))
        assertThat(active.tle.fetchedAt).isEqualTo(NOW)
    }

    @Test
    fun `rejects a line shorter than 69 characters`() {
        assertThatThrownBy { parser.parseElements("1 25544U", line2()) }
            .isInstanceOf(InvalidTleException::class.java)
    }

    @Test
    fun `rejects wrong line numbers`() {
        assertThatThrownBy { parser.parseElements("2" + line1().drop(1), line2()) }
            .isInstanceOf(InvalidTleException::class.java)
    }

    @Test
    fun `rejects a non-positive mean motion`() {
        assertThatThrownBy { parser.parseElements(line1(), line2(meanMotion = 0.0)) }
            .isInstanceOf(InvalidTleException::class.java)
    }
}
