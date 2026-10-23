package com.artemwhite.user.domain

import com.artemwhite.user.domain.models.PasswordHash
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class PasswordHashTest {

    @Test
    fun `rejects a blank hash`() {
        assertThatThrownBy { PasswordHash("  ") }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `masks the value in toString`() {
        assertThat(PasswordHash("secret").toString()).isEqualTo("PasswordHash(***)")
    }
}
