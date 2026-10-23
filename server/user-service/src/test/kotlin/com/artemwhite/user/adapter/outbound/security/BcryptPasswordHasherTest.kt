package com.artemwhite.user.adapter.outbound.security

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BcryptPasswordHasherTest {

    private val hasher = BcryptPasswordHasher()

    @Test
    fun `hashes a password with a bcrypt hash`() {
        val hash = hasher.hash("s3cret-password")

        assertThat(hash.value).isNotBlank()
        assertThat(hash.value).startsWith("\$2")
        assertThat(hash.toString()).isEqualTo("PasswordHash(***)")
    }

    @Test
    fun `verifies a matching password`() {
        val hash = hasher.hash("s3cret-password")

        assertThat(hasher.verify("s3cret-password", hash)).isTrue()
    }

    @Test
    fun `rejects a non-matching password`() {
        val hash = hasher.hash("s3cret-password")

        assertThat(hasher.verify("wrong-password", hash)).isFalse()
    }
}
