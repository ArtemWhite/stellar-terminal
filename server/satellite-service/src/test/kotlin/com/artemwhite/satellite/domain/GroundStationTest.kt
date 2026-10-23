package com.artemwhite.satellite.domain

import com.artemwhite.satellite.domain.models.GroundStation
import com.artemwhite.satellite.domain.models.Position
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test

class GroundStationTest {

    private val station = GroundStation.northPole()
    private val r = GroundStation.EARTH_RADIUS_KM

    @Test
    fun `a satellite directly overhead is at 90 degrees`() {
        val elevation = station.elevationDeg(Position(0.0, 0.0, 2 * r, 0.0, 0.0, 0.0))

        assertThat(elevation).isCloseTo(90.0, within(1e-9))
    }

    @Test
    fun `a satellite on the horizon is at 0 degrees`() {
        val elevation = station.elevationDeg(Position(2 * r, 0.0, r, 0.0, 0.0, 0.0))

        assertThat(elevation).isCloseTo(0.0, within(1e-9))
    }

    @Test
    fun `a satellite below the station is at negative elevation`() {
        val elevation = station.elevationDeg(Position(0.0, 0.0, 0.0, 0.0, 0.0, 0.0))

        assertThat(elevation).isCloseTo(-90.0, within(1e-9))
    }
}
