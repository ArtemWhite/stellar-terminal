package com.artemwhite.catalog.application.usecase

import com.artemwhite.catalog.domain.exceptions.ObjectNotFoundException
import com.artemwhite.catalog.testutil.FakeSpaceObjectRepository
import com.artemwhite.catalog.testutil.SpaceObjectFixtures.satellite
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.UUID

class DeleteObjectUseCaseTest {

    private val repository = FakeSpaceObjectRepository()
    private val useCase = DeleteObjectUseCase(repository)

    @Test
    fun `deletes an existing object`() {
        val obj = satellite()
        repository.save(obj)

        useCase.execute(obj.id)

        assertThat(repository.findById(obj.id)).isNull()
    }

    @Test
    fun `throws when the object does not exist`() {
        assertThatThrownBy { useCase.execute(UUID.randomUUID()) }
            .isInstanceOf(ObjectNotFoundException::class.java)
    }
}
