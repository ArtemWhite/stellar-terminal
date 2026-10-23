package com.artemwhite.satellite.application.usecase

import com.artemwhite.satellite.domain.exceptions.SatelliteNotFoundException
import com.artemwhite.satellite.domain.models.GroundStation
import com.artemwhite.satellite.domain.models.PassQuery
import com.artemwhite.satellite.domain.models.Position
import com.artemwhite.satellite.domain.ports.outbound.Clock
import com.artemwhite.satellite.domain.ports.outbound.PositionCalculator
import com.artemwhite.satellite.testutil.FakeSatelliteRepository
import com.artemwhite.satellite.testutil.SatelliteFixtures.ISS_OBJECT_ID
import com.artemwhite.satellite.testutil.SatelliteFixtures.ISS_TLE
import com.artemwhite.satellite.testutil.SatelliteFixtures.NOW
import com.artemwhite.satellite.testutil.SatelliteFixtures.satellite
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class PredictPassUseCaseTest {

    private val repository = FakeSatelliteRepository()
    private val clock = Clock { NOW }

    private val zenith = Position(0.0, 0.0, 2 * GroundStation.EARTH_RADIUS_KM, 0.0, 0.0, 0.0)
    private val nadir = Position(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

    private fun seeded() {
        repository.saveSatellite(satellite())
        repository.saveTle(ISS_OBJECT_ID, ISS_TLE)
    }

    private fun useCase(calculator: PositionCalculator) =
        PredictPassUseCase(repository, calculator, clock)

    @Test
    fun `groups contiguous above-horizon samples into a single pass`() {
        seeded()
        val calculator = PositionCalculator { _, at ->
            if (at.isBefore(NOW.plusSeconds(180))) zenith else nadir
        }

        val passes = useCase(calculator).execute(
            ISS_OBJECT_ID,
            PassQuery.builder().windowSeconds(300).stepSeconds(60).build(),
        )

        assertThat(passes).hasSize(1)
        assertThat(passes[0].objectId).isEqualTo(ISS_OBJECT_ID)
        assertThat(passes[0].start).isEqualTo(NOW)
        assertThat(passes[0].end).isEqualTo(NOW.plusSeconds(180))
        assertThat(passes[0].maxElevation).isEqualTo(90.0)
    }

    @Test
    fun `returns no passes when the satellite never rises above the horizon`() {
        seeded()
        val calculator = PositionCalculator { _, _ -> nadir }

        val passes = useCase(calculator).execute(
            ISS_OBJECT_ID,
            PassQuery.builder().windowSeconds(300).stepSeconds(60).build(),
        )

        assertThat(passes).isEmpty()
    }

    @Test
    fun `closes a pass still in progress at the end of the window`() {
        seeded()
        val calculator = PositionCalculator { _, _ -> zenith }

        val passes = useCase(calculator).execute(
            ISS_OBJECT_ID,
            PassQuery.builder().windowSeconds(300).stepSeconds(60).build(),
        )

        assertThat(passes).hasSize(1)
        assertThat(passes[0].start).isEqualTo(NOW)
        assertThat(passes[0].end).isEqualTo(NOW.plusSeconds(300))
    }

    @Test
    fun `throws when there is no TLE`() {
        repository.saveSatellite(satellite())

        assertThatThrownBy {
            useCase(PositionCalculator { _, _ -> zenith })
                .execute(ISS_OBJECT_ID, PassQuery.builder().build())
        }.isInstanceOf(SatelliteNotFoundException::class.java)
    }
}
