package com.artemwhite.catalog.application.usecase

import com.artemwhite.catalog.domain.exceptions.ObjectNotFoundException
import com.artemwhite.catalog.domain.models.SpaceObject
import com.artemwhite.catalog.domain.ports.outbound.SpaceObjectRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetObjectUseCase
    (
    private val repository: SpaceObjectRepository
) {
    fun execute(id: UUID): SpaceObject =
        repository.findById(id) ?: throw ObjectNotFoundException(id)
}