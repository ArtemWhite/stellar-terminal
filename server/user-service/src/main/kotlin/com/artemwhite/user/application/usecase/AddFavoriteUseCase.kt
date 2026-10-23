package com.artemwhite.user.application.usecase

import com.artemwhite.user.domain.exceptions.FavoriteAlreadyExistsException
import com.artemwhite.user.domain.models.Favorite
import com.artemwhite.user.domain.ports.outbound.Clock
import com.artemwhite.user.domain.ports.outbound.FavoriteRepository
import com.artemwhite.user.domain.ports.outbound.ObjectRegistryClient
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AddFavoriteUseCase(
    private val favoriteRepository: FavoriteRepository,
    private val registry: ObjectRegistryClient,
    private val clock: Clock,
) {
    fun execute(userId: UUID, objectId: UUID): ResolvedFavorite {
        if (favoriteRepository.exists(userId, objectId)) {
            throw FavoriteAlreadyExistsException(userId, objectId)
        }
        val favorite = favoriteRepository.save(Favorite.create(userId, objectId, clock.now()))
        return ResolvedFavorite(favorite, registry.getObject(objectId))
    }
}
