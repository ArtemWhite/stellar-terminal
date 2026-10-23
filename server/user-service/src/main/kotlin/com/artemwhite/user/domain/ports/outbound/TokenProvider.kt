package com.artemwhite.user.domain.ports.outbound

import com.artemwhite.user.domain.models.Role
import java.util.UUID

interface TokenProvider {
    fun issue(userId: UUID, role: Role): String
}
