package com.artemwhite.user.application.usecase

import com.artemwhite.user.domain.models.Favorite
import com.artemwhite.user.domain.models.PageResult
import com.artemwhite.user.domain.ports.outbound.FavoriteRepository
import com.artemwhite.user.domain.ports.outbound.ObjectRegistryClient
import com.artemwhite.shared.model.ObjectRef
import org.springframework.stereotype.Service
import java.util.UUID

data class ResolvedFavorite(val favorite: Favorite, val objectRef: ObjectRef)

@Service
class ListFavoritesUseCase(
    private val favoriteRepository: FavoriteRepository,
    private val registry: ObjectRegistryClient,
) {
    fun execute(userId: UUID, page: Int, size: Int): PageResult<ResolvedFavorite> {
        val result = favoriteRepository.findByUser(userId, page, size)
        return PageResult(
            items = result.items.map { ResolvedFavorite(it, registry.getObject(it.objectId)) },
            page = result.page,
            size = result.size,
            total = result.total,
        )
    }
}
