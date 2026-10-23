package com.artemwhite.catalog.testutil

import com.artemwhite.catalog.domain.models.PageResult
import com.artemwhite.catalog.domain.models.SearchFilter
import com.artemwhite.catalog.domain.models.SpaceObject
import com.artemwhite.catalog.domain.ports.outbound.SpaceObjectRepository
import java.util.UUID

/**
 * Hand-rolled in-memory fake of the repository port. Used in unit tests so that
 * the application layer can be tested without a database or a mocking library.
 */
class FakeSpaceObjectRepository : SpaceObjectRepository {

    val store = LinkedHashMap<UUID, SpaceObject>()

    override fun findById(id: UUID): SpaceObject? = store[id]

    override fun existsById(id: UUID): Boolean = store.containsKey(id)

    override fun search(filter: SearchFilter): PageResult<SpaceObject> {
        val filtered = store.values.filter { obj ->
            (filter.category == null || obj.category == filter.category) &&
                (filter.status == null || obj.status == filter.status) &&
                (filter.query.isNullOrBlank() || obj.name.contains(filter.query, ignoreCase = true))
        }
        val from = filter.page * filter.size
        return PageResult(
            items = filtered.drop(from).take(filter.size),
            page = filter.page,
            size = filter.size,
            total = filtered.size.toLong(),
        )
    }

    override fun save(spaceObject: SpaceObject): SpaceObject {
        store[spaceObject.id] = spaceObject
        return spaceObject
    }

    override fun deleteById(id: UUID) {
        store.remove(id)
    }
}
