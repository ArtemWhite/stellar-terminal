package com.artemwhite.user.domain

import com.artemwhite.user.domain.models.Email
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class EmailTest {

    @Test
    fun `accepts a well-formed address`() {
        assertThat(Email("user@example.com").value).isEqualTo("user@example.com")
    }

    @Test
    fun `rejects a malformed address`() {
        assertThatThrownBy { Email("not-an-email") }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `compares by value and renders as the raw address`() {
        val a = Email("a@b.co")
        val b = Email("a@b.co")

        assertThat(a).isEqualTo(b)
        assertThat(a.hashCode()).isEqualTo(b.hashCode())
        assertThat(a.toString()).isEqualTo("a@b.co")
    }
}
