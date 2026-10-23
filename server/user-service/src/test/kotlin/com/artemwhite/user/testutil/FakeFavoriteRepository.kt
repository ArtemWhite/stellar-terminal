package com.artemwhite.user.testutil

import com.artemwhite.user.domain.models.Favorite
import com.artemwhite.user.domain.models.PageResult
import com.artemwhite.user.domain.ports.outbound.FavoriteRepository
import java.util.UUID

class FakeFavoriteRepository : FavoriteRepository {

    val store = LinkedHashMap<Pair<UUID, UUID>, Favorite>()

    override fun findByUser(userId: UUID, page: Int, size: Int): PageResult<Favorite> {
        val all = store.values.filter { it.userId == userId }.sortedByDescending { it.addedAt }
        val from = page * size
        return PageResult(
            items = all.drop(from).take(size),
            page = page,
            size = size,
            total = all.size.toLong(),
        )
    }

    override fun findUserIdsByObject(objectId: UUID): List<UUID> =
        store.values.filter { it.objectId == objectId }.map { it.userId }

    override fun exists(userId: UUID, objectId: UUID): Boolean = store.containsKey(userId to objectId)

    override fun save(favorite: Favorite): Favorite {
        store[favorite.userId to favorite.objectId] = favorite
        return favorite
    }

    override fun delete(userId: UUID, objectId: UUID) {
        store.remove(userId to objectId)
    }
}
