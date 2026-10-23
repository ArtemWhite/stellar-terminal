package com.artemwhite.satellite.domain

import com.artemwhite.satellite.domain.models.PassQuery
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class PassQueryTest {

    @Test
    fun `builder applies defaults`() {
        val query = PassQuery.builder().build()

        assertThat(query.minElevationDeg).isEqualTo(10.0)
        assertThat(query.windowSeconds).isEqualTo(86_400L)
        assertThat(query.stepSeconds).isEqualTo(60L)
    }

    @Test
    fun `builder applies custom values`() {
        val query = PassQuery.builder()
            .minElevationDeg(25.0)
            .windowSeconds(3_600L)
            .stepSeconds(30L)
            .build()

        assertThat(query.minElevationDeg).isEqualTo(25.0)
        assertThat(query.windowSeconds).isEqualTo(3_600L)
        assertThat(query.stepSeconds).isEqualTo(30L)
    }

    @Test
    fun `rejects an elevation outside the valid range`() {
        assertThatThrownBy { PassQuery.builder().minElevationDeg(91.0).build() }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `rejects a non-positive window`() {
        assertThatThrownBy { PassQuery.builder().windowSeconds(0).build() }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `rejects a non-positive step`() {
        assertThatThrownBy { PassQuery.builder().stepSeconds(0).build() }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `rejects a step larger than the window`() {
        assertThatThrownBy { PassQuery.builder().windowSeconds(60).stepSeconds(61).build() }
            .isInstanceOf(IllegalArgumentException::class.java)
    }
}
