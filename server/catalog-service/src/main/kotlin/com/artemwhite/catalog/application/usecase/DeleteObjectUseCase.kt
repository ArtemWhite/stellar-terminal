package com.artemwhite.catalog.application.usecase

import com.artemwhite.catalog.domain.exceptions.ObjectNotFoundException
import com.artemwhite.catalog.domain.ports.outbound.SpaceObjectRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DeleteObjectUseCase(
    private val repository: SpaceObjectRepository,
) {
    @Transactional
    fun execute(id: UUID) {
        if (!repository.existsById(id)) throw ObjectNotFoundException(id)
        repository.deleteById(id)
    }
}
