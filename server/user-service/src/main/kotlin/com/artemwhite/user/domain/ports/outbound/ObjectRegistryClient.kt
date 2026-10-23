package com.artemwhite.user.domain.ports.outbound

import com.artemwhite.shared.model.ObjectRef
import java.util.UUID

interface ObjectRegistryClient {
    fun getObject(objectId: UUID): ObjectRef
}
