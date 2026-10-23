package com.artemwhite.satellite.testutil

import com.artemwhite.satellite.domain.exceptions.ObjectNotFoundException
import com.artemwhite.satellite.domain.ports.outbound.ObjectRegistryClient
import com.artemwhite.shared.model.ObjectRef
import com.artemwhite.shared.model.enums.ObjectStatus
import java.util.UUID

/**
 * Hand-rolled fake of the catalog-service port. Records status updates so tests
 * can assert the sync flow propagated a decay to the source of truth.
 */
class FakeObjectRegistryClient : ObjectRegistryClient {

    val objects = LinkedHashMap<UUID, ObjectRef>()
    val statusUpdates = mutableListOf<Pair<UUID, ObjectStatus>>()

    override fun getObject(objectId: UUID): ObjectRef =
        objects[objectId] ?: throw ObjectNotFoundException(objectId)

    override fun updateStatus(objectId: UUID, status: ObjectStatus) {
        statusUpdates += objectId to status
    }
}
