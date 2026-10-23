package com.artemwhite.catalog.adapter.outbound.persistence.mappers

import com.artemwhite.catalog.adapter.outbound.persistence.entities.SpaceObjectEntity
import com.artemwhite.catalog.domain.models.SpaceObject

fun SpaceObjectEntity.toDomain(): SpaceObject = SpaceObject(
    id = id,
    name = name,
    category = category,
    subcategory = subcategory,
    description = description,
    sourceId = sourceId,
    sourceType = sourceType,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun SpaceObject.toEntity(): SpaceObjectEntity = SpaceObjectEntity(
    id = id,
    name = name,
    category = category,
    subcategory = subcategory,
    description = description,
    sourceId = sourceId,
    sourceType = sourceType,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
