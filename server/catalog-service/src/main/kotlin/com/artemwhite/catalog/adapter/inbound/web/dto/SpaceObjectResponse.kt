package com.artemwhite.catalog.adapter.inbound.web.dto

import com.artemwhite.shared.model.enums.ObjectCategory
import com.artemwhite.shared.model.enums.ObjectStatus
import com.artemwhite.shared.model.enums.SatelliteSubcategory
import com.artemwhite.shared.model.enums.SourceType
import java.time.Instant
import java.util.UUID

data class SpaceObjectResponse(
    val id: UUID,
    val name: String,
    val category: ObjectCategory,
    val subcategory: SatelliteSubcategory?,
    val description: String,
    val sourceId: String,
    val sourceType: SourceType,
    val status: ObjectStatus,
    val createdAt: Instant,
    val updatedAt: Instant,
)
