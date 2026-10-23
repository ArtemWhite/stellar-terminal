package com.artemwhite.catalog.application.command

import com.artemwhite.shared.model.enums.ObjectCategory
import com.artemwhite.shared.model.enums.SatelliteSubcategory
import com.artemwhite.shared.model.enums.SourceType

data class CreateObjectCommand
    (
    val name: String,
    val category: ObjectCategory,
    val subcategory: SatelliteSubcategory?,
    val description: String,
    val sourceId: String,
    val sourceType: SourceType
)