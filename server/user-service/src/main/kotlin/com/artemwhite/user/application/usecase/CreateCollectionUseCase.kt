package com.artemwhite.user.application.usecase

import com.artemwhite.user.application.command.CreateCollectionCommand
import com.artemwhite.user.domain.models.Collection
import com.artemwhite.user.domain.ports.outbound.CollectionRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CreateCollectionUseCase(
    private val collectionRepository: CollectionRepository,
) {
    fun execute(command: CreateCollectionCommand): Collection =
        collectionRepository.save(Collection.create(UUID.randomUUID(), command.userId, command.name))
}
