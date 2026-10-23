package com.artemwhite.user.domain

import com.artemwhite.user.domain.models.Role
import com.artemwhite.user.domain.models.User
import com.artemwhite.user.testutil.UserFixtures.NOW
import com.artemwhite.user.testutil.UserFixtures.email
import com.artemwhite.user.testutil.UserFixtures.user
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.UUID

class UserTest {

    @Test
    fun `creates a REGISTERED user with the given identity`() {
        val id = UUID.randomUUID()
        val email = email()

        val created = User.create(id, "tester", email, com.artemwhite.user.domain.models.PasswordHash("h"), Role.REGISTERED, NOW)

        assertThat(created.id).isEqualTo(id)
        assertThat(created.nickname).isEqualTo("tester")
        assertThat(created.email).isEqualTo(email)
        assertThat(created.role).isEqualTo(Role.REGISTERED)
        assertThat(created.createdAt).isEqualTo(NOW)
    }

    @Test
    fun `rejects a blank nickname`() {
        assertThatThrownBy { user(nickname = "  ") }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `changes role through the behaviour method`() {
        val u = user()

        u.changeRole(Role.ADMIN)

        assertThat(u.role).isEqualTo(Role.ADMIN)
    }

    @Test
    fun `masks the password hash in toString`() {
        val u = user()

        assertThat(u.passwordHash.toString()).isEqualTo("PasswordHash(***)")
    }
}
