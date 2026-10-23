package com.artemwhite.user.application.usecase

import com.artemwhite.user.domain.exceptions.CollectionNotFoundException
import com.artemwhite.user.domain.ports.outbound.CollectionRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class DeleteCollectionUseCase(
    private val collectionRepository: CollectionRepository,
) {
    fun execute(id: UUID) {
        if (collectionRepository.findById(id) == null) {
            throw CollectionNotFoundException(id)
        }
        collectionRepository.delete(id)
    }
}
