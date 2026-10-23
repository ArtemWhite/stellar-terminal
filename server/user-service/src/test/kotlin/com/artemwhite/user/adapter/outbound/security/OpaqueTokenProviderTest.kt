package com.artemwhite.user.adapter.outbound.security

import com.artemwhite.user.domain.models.Role
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class OpaqueTokenProviderTest {

    private val provider = OpaqueTokenProvider()

    @Test
    fun `issues a 64 character hex token`() {
        val token = provider.issue(UUID.randomUUID(), Role.REGISTERED)

        assertThat(token).hasSize(64)
        assertThat(token).matches("[0-9a-f]{64}")
    }

    @Test
    fun `issues distinct tokens on every call`() {
        val first = provider.issue(UUID.randomUUID(), Role.REGISTERED)
        val second = provider.issue(UUID.randomUUID(), Role.ADMIN)

        assertThat(first).isNotEqualTo(second)
    }
}
