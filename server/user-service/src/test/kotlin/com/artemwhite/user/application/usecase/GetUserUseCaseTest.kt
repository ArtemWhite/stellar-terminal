package com.artemwhite.user.application.usecase

import com.artemwhite.user.domain.exceptions.UserNotFoundException
import com.artemwhite.user.testutil.FakeUserRepository
import com.artemwhite.user.testutil.UserFixtures.user
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.UUID

class GetUserUseCaseTest {

    private val repository = FakeUserRepository()
    private val useCase = GetUserUseCase(repository)

    @Test
    fun `returns the user by id`() {
        val u = user()
        repository.save(u)

        assertThat(useCase.execute(u.id)).isSameAs(u)
    }

    @Test
    fun `throws when the user does not exist`() {
        assertThatThrownBy { useCase.execute(UUID.randomUUID()) }
            .isInstanceOf(UserNotFoundException::class.java)
    }
}
