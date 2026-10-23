package com.artemwhite.satellite.domain.ports.outbound

import com.artemwhite.shared.model.ObjectRef
import com.artemwhite.shared.model.enums.ObjectStatus
import java.util.UUID

interface ObjectRegistryClient {
    fun getObject(objectId: UUID): ObjectRef

    fun updateStatus(objectId: UUID, status: ObjectStatus)
}
