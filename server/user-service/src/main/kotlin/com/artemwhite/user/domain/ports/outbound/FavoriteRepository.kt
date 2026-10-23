package com.artemwhite.user.domain.ports.outbound

import com.artemwhite.user.domain.models.Favorite
import com.artemwhite.user.domain.models.PageResult
import java.util.UUID

interface FavoriteRepository {
    fun findByUser(userId: UUID, page: Int, size: Int): PageResult<Favorite>

    fun findUserIdsByObject(objectId: UUID): List<UUID>

    fun exists(userId: UUID, objectId: UUID): Boolean

    fun save(favorite: Favorite): Favorite

    fun delete(userId: UUID, objectId: UUID)
}
