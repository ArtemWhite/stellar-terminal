package com.artemwhite.user.testutil

import com.artemwhite.user.domain.models.PasswordHash
import com.artemwhite.user.domain.ports.outbound.PasswordHasher

class FakePasswordHasher : PasswordHasher {
    override fun hash(rawPassword: String): PasswordHash = PasswordHash("hashed:$rawPassword")

    override fun verify(rawPassword: String, hash: PasswordHash): Boolean = hash.value == "hashed:$rawPassword"
}
