package com.artemwhite.catalog.adapter.inbound.web.mappers

import com.artemwhite.catalog.adapter.inbound.web.dto.CreateObjectRequest
import com.artemwhite.catalog.adapter.inbound.web.dto.SpaceObjectResponse
import com.artemwhite.catalog.application.command.CreateObjectCommand
import com.artemwhite.catalog.domain.models.PageResult
import com.artemwhite.catalog.domain.models.SpaceObject

fun CreateObjectRequest.toCommand(): CreateObjectCommand = CreateObjectCommand(
    name = name,
    category = category,
    subcategory = subcategory,
    description = description,
    sourceId = sourceId,
    sourceType = sourceType,
)

fun SpaceObject.toResponse(): SpaceObjectResponse = SpaceObjectResponse(
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

fun PageResult<SpaceObject>.toResponse(): PageResult<SpaceObjectResponse> = PageResult(
    items = items.map { it.toResponse() },
    page = page,
    size = size,
    total = total,
)
