package com.artemwhite.user.application.usecase

import com.artemwhite.user.domain.ports.outbound.FavoriteRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class RemoveFavoriteUseCase(
    private val favoriteRepository: FavoriteRepository,
) {
    fun execute(userId: UUID, objectId: UUID) {
        favoriteRepository.delete(userId, objectId)
    }
}
