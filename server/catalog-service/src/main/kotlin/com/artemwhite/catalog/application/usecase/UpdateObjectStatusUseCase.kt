package com.artemwhite.catalog.application.usecase

import com.artemwhite.catalog.domain.exceptions.ObjectNotFoundException
import com.artemwhite.catalog.domain.models.SpaceObject
import com.artemwhite.catalog.domain.ports.outbound.Clock
import com.artemwhite.catalog.domain.ports.outbound.SpaceObjectRepository
import com.artemwhite.shared.model.enums.ObjectStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UpdateObjectStatusUseCase(
    private val repository: SpaceObjectRepository,
    private val clock: Clock,
) {
    private val log = LoggerFactory.getLogger(UpdateObjectStatusUseCase::class.java)

    @Transactional
    fun execute(id: UUID, newStatus: ObjectStatus): SpaceObject {
        val spaceObject = repository.findById(id) ?: throw ObjectNotFoundException(id)
        spaceObject.changeStatus(newStatus, clock.now())
        val saved = repository.save(spaceObject)
        log.info("Changed status of object {} to {}", id, newStatus)
        return saved
    }
}
