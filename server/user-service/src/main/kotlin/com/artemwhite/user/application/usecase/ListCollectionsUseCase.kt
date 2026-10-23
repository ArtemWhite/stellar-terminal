package com.artemwhite.user.application.usecase

import com.artemwhite.user.domain.models.Collection
import com.artemwhite.user.domain.models.PageResult
import com.artemwhite.user.domain.ports.outbound.CollectionRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ListCollectionsUseCase(
    private val collectionRepository: CollectionRepository,
) {
    fun execute(userId: UUID, page: Int, size: Int): PageResult<Collection> =
        collectionRepository.findByUser(userId, page, size)
}
