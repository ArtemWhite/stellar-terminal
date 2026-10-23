package com.artemwhite.catalog.application.usecase

import com.artemwhite.catalog.domain.exceptions.ObjectNotFoundException
import com.artemwhite.catalog.testutil.FakeSpaceObjectRepository
import com.artemwhite.catalog.testutil.SpaceObjectFixtures.satellite
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.UUID

class GetObjectUseCaseTest {

    private val repository = FakeSpaceObjectRepository()
    private val useCase = GetObjectUseCase(repository)

    @Test
    fun `returns the object when it exists`() {
        val obj = satellite()
        repository.save(obj)

        val result = useCase.execute(obj.id)

        assertThat(result).isSameAs(obj)
    }

    @Test
    fun `throws when the object does not exist`() {
        assertThatThrownBy { useCase.execute(UUID.randomUUID()) }
            .isInstanceOf(ObjectNotFoundException::class.java)
    }
}
