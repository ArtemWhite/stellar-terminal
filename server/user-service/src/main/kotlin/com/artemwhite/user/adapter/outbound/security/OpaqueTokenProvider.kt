package com.artemwhite.user.adapter.outbound.security

import com.artemwhite.user.domain.models.Role
import com.artemwhite.user.domain.ports.outbound.TokenProvider
import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.util.UUID

@Component
class OpaqueTokenProvider : TokenProvider {
    private val random = SecureRandom()

    override fun issue(userId: UUID, role: Role): String {
        val bytes = ByteArray(TOKEN_BYTES)
        random.nextBytes(bytes)
        return bytes.joinToString("") { "%02x".format(it) }
    }

    companion object {
        private const val TOKEN_BYTES = 32
    }
}
