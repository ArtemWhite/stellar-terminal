package com.artemwhite.user.application.usecase

import com.artemwhite.user.application.command.RegisterUserCommand
import com.artemwhite.user.domain.exceptions.DuplicateEmailException
import com.artemwhite.user.domain.models.Email
import com.artemwhite.user.domain.models.Role
import com.artemwhite.user.domain.ports.outbound.Clock
import com.artemwhite.user.testutil.FakePasswordHasher
import com.artemwhite.user.testutil.FakeUserRepository
import com.artemwhite.user.testutil.UserFixtures.NOW
import com.artemwhite.user.testutil.UserFixtures.email
import com.artemwhite.user.testutil.UserFixtures.user
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class RegisterUserUseCaseTest {

    private val repository = FakeUserRepository()
    private val hasher = FakePasswordHasher()
    private val clock = Clock { NOW }
    private val useCase = RegisterUserUseCase(repository, hasher, clock)

    @Test
    fun `registers a REGISTERED user with a hashed password`() {
        val command = RegisterUserCommand("tester", Email("test@example.com"), "password123")

        val created = useCase.execute(command)

        assertThat(created.nickname).isEqualTo("tester")
        assertThat(created.role).isEqualTo(Role.REGISTERED)
        assertThat(created.passwordHash.value).isEqualTo("hashed:password123")
        assertThat(created.createdAt).isEqualTo(NOW)
        assertThat(repository.findById(created.id)).isSameAs(created)
    }

    @Test
    fun `rejects a duplicate email`() {
        repository.save(user(email = email("dup@example.com")))

        assertThatThrownBy { useCase.execute(RegisterUserCommand("x", Email("dup@example.com"), "password123")) }
            .isInstanceOf(DuplicateEmailException::class.java)
    }
}
