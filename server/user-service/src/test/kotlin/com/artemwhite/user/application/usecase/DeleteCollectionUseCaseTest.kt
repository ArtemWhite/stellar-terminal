package com.artemwhite.user.application.usecase

import com.artemwhite.user.domain.exceptions.CollectionNotFoundException
import com.artemwhite.user.testutil.FakeCollectionRepository
import com.artemwhite.user.testutil.UserFixtures.collection
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.UUID

class DeleteCollectionUseCaseTest {

    private val repository = FakeCollectionRepository()
    private val useCase = DeleteCollectionUseCase(repository)

    @Test
    fun `deletes the collection`() {
        val c = collection(userId = UUID.randomUUID())
        repository.save(c)

        useCase.execute(c.id)

        assertThat(repository.findById(c.id)).isNull()
    }

    @Test
    fun `throws when the collection does not exist`() {
        assertThatThrownBy { useCase.execute(UUID.randomUUID()) }
            .isInstanceOf(CollectionNotFoundException::class.java)
    }
}
