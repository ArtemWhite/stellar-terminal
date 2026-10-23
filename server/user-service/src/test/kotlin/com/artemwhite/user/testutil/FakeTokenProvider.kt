package com.artemwhite.user.testutil

import com.artemwhite.user.domain.models.Role
import com.artemwhite.user.domain.ports.outbound.TokenProvider
import java.util.UUID

class FakeTokenProvider : TokenProvider {
    override fun issue(userId: UUID, role: Role): String = "token-for-$userId-${role.name}"
}
