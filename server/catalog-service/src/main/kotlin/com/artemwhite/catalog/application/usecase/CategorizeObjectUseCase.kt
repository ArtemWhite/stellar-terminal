package com.artemwhite.catalog.application.usecase

import com.artemwhite.catalog.domain.exceptions.ObjectNotFoundException
import com.artemwhite.catalog.domain.models.SpaceObject
import com.artemwhite.catalog.domain.ports.outbound.Clock
import com.artemwhite.catalog.domain.ports.outbound.SpaceObjectRepository
import com.artemwhite.shared.model.enums.ObjectCategory
import com.artemwhite.shared.model.enums.SatelliteSubcategory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class CategorizeObjectUseCase(
    private val repository: SpaceObjectRepository,
    private val clock: Clock,
) {
    @Transactional
    fun execute(
        id: UUID,
        newCategory: ObjectCategory,
        newSubcategory: SatelliteSubcategory?,
    ): SpaceObject {
        val spaceObject = repository.findById(id) ?: throw ObjectNotFoundException(id)
        spaceObject.recategorize(newCategory, newSubcategory, clock.now())
        return repository.save(spaceObject)
    }
}
