package com.artemwhite.user.application.usecase

import com.artemwhite.user.testutil.FakeFavoriteRepository
import com.artemwhite.user.testutil.UserFixtures.NOW
import com.artemwhite.user.testutil.UserFixtures.OBJECT_ID
import com.artemwhite.user.testutil.UserFixtures.favorite
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class RemoveFavoriteUseCaseTest {

    private val repository = FakeFavoriteRepository()
    private val useCase = RemoveFavoriteUseCase(repository)

    @Test
    fun `removes the favorite if it exists`() {
        val userId = UUID.randomUUID()
        repository.save(favorite(userId, OBJECT_ID, NOW))

        useCase.execute(userId, OBJECT_ID)

        assertThat(repository.exists(userId, OBJECT_ID)).isFalse()
    }

    @Test
    fun `is idempotent when the favorite is absent`() {
        useCase.execute(UUID.randomUUID(), UUID.randomUUID())

        assertThat(repository.store).isEmpty()
    }
}
