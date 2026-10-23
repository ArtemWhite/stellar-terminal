package com.artemwhite.catalog.adapter.inbound.web.dto

import com.artemwhite.shared.model.enums.ObjectCategory
import com.artemwhite.shared.model.enums.SatelliteSubcategory
import com.artemwhite.shared.model.enums.SourceType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateObjectRequest(
    @field:NotBlank val name: String,
    @field:NotNull val category: ObjectCategory,
    val subcategory: SatelliteSubcategory?,
    @field:NotBlank val description: String,
    @field:NotBlank val sourceId: String,
    @field:NotNull val sourceType: SourceType,
)
