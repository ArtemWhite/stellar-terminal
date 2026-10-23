package com.artemwhite.user.application.usecase

import com.artemwhite.user.testutil.FakeCollectionRepository
import com.artemwhite.user.testutil.UserFixtures.collection
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class ListCollectionsUseCaseTest {

    private val repository = FakeCollectionRepository()
    private val useCase = ListCollectionsUseCase(repository)

    @Test
    fun `returns the collections of a user as a page`() {
        val userId = UUID.randomUUID()
        repository.save(collection(userId = userId, name = "B"))
        repository.save(collection(userId = userId, name = "A"))
        repository.save(collection(userId = UUID.randomUUID(), name = "C"))

        val result = useCase.execute(userId, 0, 20)

        assertThat(result.items.map { it.name }).containsExactly("A", "B")
        assertThat(result.total).isEqualTo(2)
    }

    @Test
    fun `paginates collections`() {
        val userId = UUID.randomUUID()
        repeat(3) { repository.save(collection(userId = userId, name = "c$it")) }

        val first = useCase.execute(userId, 0, 2)
        val second = useCase.execute(userId, 1, 2)

        assertThat(first.items).hasSize(2)
        assertThat(second.items).hasSize(1)
        assertThat(first.total).isEqualTo(3)
    }
}
