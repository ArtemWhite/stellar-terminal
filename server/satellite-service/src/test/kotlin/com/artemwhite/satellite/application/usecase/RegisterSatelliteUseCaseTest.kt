package com.artemwhite.satellite.application.usecase

import com.artemwhite.satellite.application.command.RegisterSatelliteCommand
import com.artemwhite.satellite.testutil.FakeSatelliteRepository
import com.artemwhite.satellite.testutil.SatelliteFixtures.ISS_OBJECT_ID
import com.artemwhite.satellite.testutil.SatelliteFixtures.NORAD_ISS
import com.artemwhite.shared.model.enums.ObjectStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RegisterSatelliteUseCaseTest {

    private val repository = FakeSatelliteRepository()
    private val useCase = RegisterSatelliteUseCase(repository)

    @Test
    fun `registers a new satellite as ACTIVE`() {
        val command = RegisterSatelliteCommand(ISS_OBJECT_ID, NORAD_ISS)

        val result = useCase.execute(command)

        assertThat(result.objectId).isEqualTo(ISS_OBJECT_ID)
        assertThat(result.noradId).isEqualTo(NORAD_ISS)
        assertThat(result.status).isEqualTo(ObjectStatus.ACTIVE)
        assertThat(repository.findSatellite(ISS_OBJECT_ID)).isSameAs(result)
    }

    @Test
    fun `registration is idempotent`() {
        val command = RegisterSatelliteCommand(ISS_OBJECT_ID, NORAD_ISS)
        val first = useCase.execute(command)

        val second = useCase.execute(command)

        assertThat(second).isSameAs(first)
    }
}
