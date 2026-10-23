package com.artemwhite.user.application.usecase

import com.artemwhite.user.domain.exceptions.CollectionNotFoundException
import com.artemwhite.user.testutil.FakeCollectionRepository
import com.artemwhite.user.testutil.UserFixtures.OBJECT_ID
import com.artemwhite.user.testutil.UserFixtures.collection
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.UUID

class RemoveObjectFromCollectionUseCaseTest {

    private val repository = FakeCollectionRepository()
    private val useCase = RemoveObjectFromCollectionUseCase(repository)

    @Test
    fun `removes an object from the collection`() {
        val c = collection(userId = UUID.randomUUID(), objectIds = listOf(OBJECT_ID))
        repository.save(c)

        val updated = useCase.execute(c.id, OBJECT_ID)

        assertThat(updated.objectIds).isEmpty()
    }

    @Test
    fun `throws when the collection does not exist`() {
        assertThatThrownBy { useCase.execute(UUID.randomUUID(), OBJECT_ID) }
            .isInstanceOf(CollectionNotFoundException::class.java)
    }
}
