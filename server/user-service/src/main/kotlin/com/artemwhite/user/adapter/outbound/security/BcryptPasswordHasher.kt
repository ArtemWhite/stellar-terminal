package com.artemwhite.user.adapter.outbound.security

import com.artemwhite.user.domain.models.PasswordHash
import com.artemwhite.user.domain.ports.outbound.PasswordHasher
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class BcryptPasswordHasher : PasswordHasher {
    private val encoder = BCryptPasswordEncoder()

    override fun hash(rawPassword: String): PasswordHash =
        PasswordHash(requireNotNull(encoder.encode(rawPassword)) { "password encoder returned null" })

    override fun verify(rawPassword: String, hash: PasswordHash): Boolean =
        runCatching { encoder.matches(rawPassword, hash.value) }.getOrDefault(false)
}
