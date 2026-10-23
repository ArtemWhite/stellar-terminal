package com.artemwhite.user.application.usecase

import com.artemwhite.user.domain.exceptions.CollectionNotFoundException
import com.artemwhite.user.domain.models.Collection
import com.artemwhite.user.domain.ports.outbound.CollectionRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AddObjectToCollectionUseCase(
    private val collectionRepository: CollectionRepository,
) {
    fun execute(collectionId: UUID, objectId: UUID): Collection {
        val collection = collectionRepository.findById(collectionId)
            ?: throw CollectionNotFoundException(collectionId)
        collection.addObject(objectId)
        return collectionRepository.save(collection)
    }
}
