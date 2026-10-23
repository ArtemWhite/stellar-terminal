package com.artemwhite.satellite.application.usecase

import com.artemwhite.satellite.domain.exceptions.NotASatelliteException
import com.artemwhite.satellite.domain.exceptions.ObjectNotFoundException
import com.artemwhite.satellite.domain.exceptions.SatelliteNotFoundException
import com.artemwhite.satellite.domain.ports.outbound.Clock
import com.artemwhite.satellite.domain.ports.outbound.PositionCalculator
import com.artemwhite.satellite.testutil.FakeObjectRegistryClient
import com.artemwhite.satellite.testutil.FakeSatelliteRepository
import com.artemwhite.satellite.testutil.SatelliteFixtures.ISS_OBJECT_ID
import com.artemwhite.satellite.testutil.SatelliteFixtures.ISS_TLE
import com.artemwhite.satellite.testutil.SatelliteFixtures.LATER
import com.artemwhite.satellite.testutil.SatelliteFixtures.NOW
import com.artemwhite.satellite.testutil.SatelliteFixtures.SAMPLE_POSITION
import com.artemwhite.satellite.testutil.SatelliteFixtures.objectRef
import com.artemwhite.satellite.testutil.SatelliteFixtures.satellite
import com.artemwhite.shared.model.enums.ObjectCategory
import java.time.Instant
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class GetPositionUseCaseTest {

    private val repository = FakeSatelliteRepository()
    private val registry = FakeObjectRegistryClient()
    private val clock = Clock { NOW }

    private fun seeded() {
        val sat = satellite()
        repository.saveSatellite(sat)
        repository.saveTle(sat.objectId, ISS_TLE)
        registry.objects[sat.objectId] = objectRef(sat.objectId)
    }

    private fun useCase(calculator: PositionCalculator) =
        GetPositionUseCase(repository, registry, calculator, clock)

    @Test
    fun `propagates the TLE to the requested instant`() {
        seeded()
        var recordedAt: Instant? = null
        val calculator = PositionCalculator { _, at -> recordedAt = at; SAMPLE_POSITION }

        val result = useCase(calculator).execute(ISS_OBJECT_ID, LATER)

        assertThat(result).isEqualTo(SAMPLE_POSITION)
        assertThat(recordedAt).isEqualTo(LATER)
    }

    @Test
    fun `defaults to the clock when no instant is given`() {
        seeded()
        var recordedAt: Instant? = null
        val calculator = PositionCalculator { _, at -> recordedAt = at; SAMPLE_POSITION }

        useCase(calculator).execute(ISS_OBJECT_ID)

        assertThat(recordedAt).isEqualTo(NOW)
    }

    @Test
    fun `throws when the catalog does not know the object`() {
        seeded()

        val useCase = useCase(PositionCalculator { _, _ -> SAMPLE_POSITION })

        assertThatThrownBy { useCase.execute(UUID.randomUUID()) }
            .isInstanceOf(ObjectNotFoundException::class.java)
    }

    @Test
    fun `throws when the object is not a satellite`() {
        seeded()
        val starId = UUID.randomUUID()
        registry.objects[starId] = objectRef(starId, ObjectCategory.STAR)

        val useCase = useCase(PositionCalculator { _, _ -> SAMPLE_POSITION })

        assertThatThrownBy { useCase.execute(starId) }
            .isInstanceOf(NotASatelliteException::class.java)
    }

    @Test
    fun `throws when there is no TLE for the satellite`() {
        val sat = satellite()
        repository.saveSatellite(sat)
        registry.objects[sat.objectId] = objectRef(sat.objectId)
        // TLE deliberately not saved.

        val useCase = useCase(PositionCalculator { _, _ -> SAMPLE_POSITION })

        assertThatThrownBy { useCase.execute(sat.objectId) }
            .isInstanceOf(SatelliteNotFoundException::class.java)
    }
}
