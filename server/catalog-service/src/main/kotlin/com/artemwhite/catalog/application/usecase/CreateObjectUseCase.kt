package com.artemwhite.catalog.application.usecase

import com.artemwhite.catalog.application.command.CreateObjectCommand
import com.artemwhite.catalog.domain.models.SpaceObject
import com.artemwhite.catalog.domain.ports.outbound.Clock
import com.artemwhite.catalog.domain.ports.outbound.SpaceObjectRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CreateObjectUseCase(
    private val repository: SpaceObjectRepository,
    private val clock: Clock,
) {
    private val log = LoggerFactory.getLogger(CreateObjectUseCase::class.java)

    fun execute(command: CreateObjectCommand): SpaceObject {
        val spaceObject = repository.save(
            SpaceObject.create(
                id = UUID.randomUUID(),
                name = command.name,
                category = command.category,
                subcategory = command.subcategory,
                description = command.description,
                sourceId = command.sourceId,
                sourceType = command.sourceType,
                now = clock.now(),
            ),
        )
        log.info("Created object {} ({})", spaceObject.id, spaceObject.name)
        return spaceObject
    }
}
