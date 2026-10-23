package com.artemwhite.catalog.application.usecase

import com.artemwhite.catalog.domain.models.PageResult
import com.artemwhite.catalog.domain.models.SearchFilter
import com.artemwhite.catalog.domain.models.SpaceObject
import com.artemwhite.catalog.domain.ports.outbound.SpaceObjectRepository
import org.springframework.stereotype.Service

@Service
class SearchObjectsUseCase
    (private val repository: SpaceObjectRepository) {
    fun execute(filter: SearchFilter): PageResult<SpaceObject> =
        repository.search(filter)
}