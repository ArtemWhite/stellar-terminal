package com.artemwhite.user.testutil

import com.artemwhite.user.domain.models.Collection
import com.artemwhite.user.domain.models.PageResult
import com.artemwhite.user.domain.ports.outbound.CollectionRepository
import java.util.UUID

class FakeCollectionRepository : CollectionRepository {

    val store = LinkedHashMap<UUID, Collection>()

    override fun findById(id: UUID): Collection? = store[id]

    override fun findByUser(userId: UUID, page: Int, size: Int): PageResult<Collection> {
        val all = store.values.filter { it.userId == userId }.sortedBy { it.name }
        val from = page * size
        return PageResult(
            items = all.drop(from).take(size),
            page = page,
            size = size,
            total = all.size.toLong(),
        )
    }

    override fun save(collection: Collection): Collection {
        store[collection.id] = collection
        return collection
    }

    override fun delete(id: UUID) {
        store.remove(id)
    }
}
