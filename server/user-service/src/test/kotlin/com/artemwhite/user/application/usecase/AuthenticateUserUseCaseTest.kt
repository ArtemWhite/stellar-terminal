package com.artemwhite.user.application.usecase

import com.artemwhite.user.domain.exceptions.InvalidCredentialsException
import com.artemwhite.user.domain.models.Email
import com.artemwhite.user.testutil.FakePasswordHasher
import com.artemwhite.user.testutil.FakeTokenProvider
import com.artemwhite.user.testutil.FakeUserRepository
import com.artemwhite.user.testutil.UserFixtures.email
import com.artemwhite.user.testutil.UserFixtures.user
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class AuthenticateUserUseCaseTest {

    private val repository = FakeUserRepository()
    private val hasher = FakePasswordHasher()
    private val tokens = FakeTokenProvider()
    private val useCase = AuthenticateUserUseCase(repository, hasher, tokens)

    @Test
    fun `authenticates a user with matching credentials and issues a token`() {
        val u = user(email = email("test@example.com"))
        repository.save(u)

        val result = useCase.execute(Email("test@example.com"), "password123")

        assertThat(result.user.id).isEqualTo(u.id)
        assertThat(result.token).isEqualTo("token-for-${u.id}-${u.role.name}")
    }

    @Test
    fun `rejects an unknown email`() {
        assertThatThrownBy { useCase.execute(Email("nope@example.com"), "password123") }
            .isInstanceOf(InvalidCredentialsException::class.java)
    }

    @Test
    fun `rejects a wrong password`() {
        repository.save(user(email = email("test@example.com")))

        assertThatThrownBy { useCase.execute(Email("test@example.com"), "wrong-password") }
            .isInstanceOf(InvalidCredentialsException::class.java)
    }
}
