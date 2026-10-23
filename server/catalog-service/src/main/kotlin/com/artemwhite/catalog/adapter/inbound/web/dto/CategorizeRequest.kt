package com.artemwhite.catalog.adapter.inbound.web.dto

import com.artemwhite.shared.model.enums.ObjectCategory
import com.artemwhite.shared.model.enums.SatelliteSubcategory
import jakarta.validation.constraints.NotNull

data class CategorizeRequest(
    @field:NotNull val category: ObjectCategory,
    val subcategory: SatelliteSubcategory?,
)
