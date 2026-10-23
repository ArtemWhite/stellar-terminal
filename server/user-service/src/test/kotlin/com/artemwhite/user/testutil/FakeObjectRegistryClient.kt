package com.artemwhite.user.testutil

import com.artemwhite.shared.model.ObjectRef
import com.artemwhite.user.domain.exceptions.ObjectNotFoundException
import com.artemwhite.user.domain.ports.outbound.ObjectRegistryClient
import java.util.UUID

class FakeObjectRegistryClient : ObjectRegistryClient {

    val objects = LinkedHashMap<UUID, ObjectRef>()

    override fun getObject(objectId: UUID): ObjectRef =
        objects[objectId] ?: throw ObjectNotFoundException(objectId)
}
