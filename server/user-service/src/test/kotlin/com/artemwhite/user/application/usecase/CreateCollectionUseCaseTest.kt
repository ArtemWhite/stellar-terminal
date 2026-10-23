package com.artemwhite.user.application.usecase

import com.artemwhite.user.application.command.CreateCollectionCommand
import com.artemwhite.user.testutil.FakeCollectionRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class CreateCollectionUseCaseTest {

    private val repository = FakeCollectionRepository()
    private val useCase = CreateCollectionUseCase(repository)

    @Test
    fun `creates an empty collection`() {
        val userId = UUID.randomUUID()

        val created = useCase.execute(CreateCollectionCommand(userId, "Favourites"))

        assertThat(created.userId).isEqualTo(userId)
        assertThat(created.name).isEqualTo("Favourites")
        assertThat(created.objectIds).isEmpty()
        assertThat(repository.findById(created.id)).isSameAs(created)
    }
}
