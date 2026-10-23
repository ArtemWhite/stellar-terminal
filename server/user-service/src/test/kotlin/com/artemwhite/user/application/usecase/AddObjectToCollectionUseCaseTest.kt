package com.artemwhite.user.application.usecase

import com.artemwhite.user.domain.exceptions.CollectionNotFoundException
import com.artemwhite.user.testutil.FakeCollectionRepository
import com.artemwhite.user.testutil.UserFixtures.OBJECT_ID
import com.artemwhite.user.testutil.UserFixtures.collection
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.UUID

class AddObjectToCollectionUseCaseTest {

    private val repository = FakeCollectionRepository()
    private val useCase = AddObjectToCollectionUseCase(repository)

    @Test
    fun `adds an object to the collection`() {
        val c = collection(userId = UUID.randomUUID())
        repository.save(c)

        val updated = useCase.execute(c.id, OBJECT_ID)

        assertThat(updated.objectIds).containsExactly(OBJECT_ID)
    }

    @Test
    fun `throws when the collection does not exist`() {
        assertThatThrownBy { useCase.execute(UUID.randomUUID(), OBJECT_ID) }
            .isInstanceOf(CollectionNotFoundException::class.java)
    }
}
