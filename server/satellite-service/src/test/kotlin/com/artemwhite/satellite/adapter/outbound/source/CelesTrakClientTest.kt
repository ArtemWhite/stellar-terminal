package com.artemwhite.satellite.adapter.outbound.source

import com.artemwhite.satellite.adapter.outbound.calculation.TleParser
import com.artemwhite.satellite.domain.ports.outbound.Clock
import com.artemwhite.satellite.testutil.SatelliteFixtures.NORAD_ISS
import com.artemwhite.satellite.testutil.SatelliteFixtures.NOW
import com.artemwhite.satellite.testutil.SatelliteFixtures.line1
import com.artemwhite.satellite.testutil.SatelliteFixtures.line2
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CelesTrakClientTest {

    private val client = CelesTrakClient(TleParser(), Clock { NOW }, "http://unused")

    @Test
    fun `parses a multi-record payload into ActiveTle pairs`() {
        val body = listOf(
            "ISS (ZARYA)",
            line1(NORAD_ISS),
            line2(NORAD_ISS),
            "NOAA 19",
            line1(33591),
            line2(33591),
        ).joinToString("\n")

        val result = client.parseResponse(body, NOW)

        assertThat(result).hasSize(2)
        assertThat(result[0].noradId).isEqualTo(NORAD_ISS)
        assertThat(result[1].noradId).isEqualTo(33591)
        assertThat(result[0].tle.fetchedAt).isEqualTo(NOW)
    }

    @Test
    fun `skips title lines without a following TLE pair`() {
        val body = "random junk\nISS (ZARYA)\n" + line1(NORAD_ISS) + "\n" + line2(NORAD_ISS)

        val result = client.parseResponse(body, NOW)

        assertThat(result).hasSize(1)
        assertThat(result[0].noradId).isEqualTo(NORAD_ISS)
    }

    @Test
    fun `returns an empty list for an empty payload`() {
        assertThat(client.parseResponse("", NOW)).isEmpty()
    }
}
