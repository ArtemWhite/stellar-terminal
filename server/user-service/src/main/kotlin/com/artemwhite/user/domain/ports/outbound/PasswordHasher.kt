package com.artemwhite.user.domain.ports.outbound

import com.artemwhite.user.domain.models.PasswordHash

interface PasswordHasher {
    fun hash(rawPassword: String): PasswordHash

    fun verify(rawPassword: String, hash: PasswordHash): Boolean
}
