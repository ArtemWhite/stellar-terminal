package com.artemwhite.satellite.application.usecase

import com.artemwhite.satellite.domain.exceptions.SatelliteNotFoundException
import com.artemwhite.satellite.domain.models.OrbitSnapshot
import com.artemwhite.satellite.testutil.FakeSatelliteRepository
import com.artemwhite.satellite.testutil.SatelliteFixtures.NOW
import com.artemwhite.satellite.testutil.SatelliteFixtures.SAMPLE_POSITION
import com.artemwhite.satellite.testutil.SatelliteFixtures.satellite
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class GetTimelineUseCaseTest {

    private val repository = FakeSatelliteRepository()
    private val useCase = GetTimelineUseCase(repository)

    @Test
    fun `returns the snapshots of a tracked satellite as a page`() {
        val sat = satellite()
        repository.saveSatellite(sat)
        val snapshot = OrbitSnapshot(SAMPLE_POSITION, NOW)
        repository.saveSnapshot(sat.objectId, snapshot)

        val result = useCase.execute(sat.objectId, page = 0, size = 20)

        assertThat(result.items).containsExactly(snapshot)
        assertThat(result.total).isEqualTo(1)
        assertThat(result.page).isEqualTo(0)
    }

    @Test
    fun `paginates snapshots`() {
        val sat = satellite()
        repository.saveSatellite(sat)
        repeat(3) { repository.saveSnapshot(sat.objectId, OrbitSnapshot(SAMPLE_POSITION, NOW.plusSeconds(it.toLong()))) }

        val first = useCase.execute(sat.objectId, page = 0, size = 2)
        val second = useCase.execute(sat.objectId, page = 1, size = 2)

        assertThat(first.items).hasSize(2)
        assertThat(first.total).isEqualTo(3)
        assertThat(second.items).hasSize(1)
        assertThat(second.total).isEqualTo(3)
    }

    @Test
    fun `returns an empty page when there are no snapshots`() {
        val sat = satellite()
        repository.saveSatellite(sat)

        val result = useCase.execute(sat.objectId)

        assertThat(result.items).isEmpty()
        assertThat(result.total).isEqualTo(0)
    }

    @Test
    fun `throws for an untracked object`() {
        assertThatThrownBy { useCase.execute(UUID.randomUUID()) }
            .isInstanceOf(SatelliteNotFoundException::class.java)
    }
}
