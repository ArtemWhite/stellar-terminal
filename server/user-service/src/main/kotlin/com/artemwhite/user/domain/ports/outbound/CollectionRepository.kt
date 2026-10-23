package com.artemwhite.user.domain.ports.outbound

import com.artemwhite.user.domain.models.Collection
import com.artemwhite.user.domain.models.PageResult
import java.util.UUID

interface CollectionRepository {
    fun findById(id: UUID): Collection?

    fun findByUser(userId: UUID, page: Int, size: Int): PageResult<Collection>

    fun save(collection: Collection): Collection

    fun delete(id: UUID)
}
