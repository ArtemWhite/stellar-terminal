package com.artemwhite.catalog.application.usecase

import com.artemwhite.catalog.domain.exceptions.ObjectNotFoundException
import com.artemwhite.catalog.domain.ports.outbound.Clock
import com.artemwhite.catalog.testutil.FakeSpaceObjectRepository
import com.artemwhite.catalog.testutil.SpaceObjectFixtures.LATER
import com.artemwhite.catalog.testutil.SpaceObjectFixtures.satellite
import com.artemwhite.shared.model.enums.ObjectStatus
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.UUID

class UpdateObjectStatusUseCaseTest {

    private val repository = FakeSpaceObjectRepository()
    private val clock = Clock { LATER }
    private val useCase = UpdateObjectStatusUseCase(repository, clock)

    @Test
    fun `updates the status of an existing object`() {
        val obj = satellite()
        repository.save(obj)

        val result = useCase.execute(obj.id, ObjectStatus.DECAYED)

        assertThat(result.status).isEqualTo(ObjectStatus.DECAYED)
        assertThat(result.updatedAt).isEqualTo(LATER)
    }

    @Test
    fun `throws when the object does not exist`() {
        assertThatThrownBy { useCase.execute(UUID.randomUUID(), ObjectStatus.DECAYED) }
            .isInstanceOf(ObjectNotFoundException::class.java)
    }
}
