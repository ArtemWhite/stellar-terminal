package com.artemwhite.catalog.domain.exceptions

import com.artemwhite.shared.model.enums.ObjectCategory
import com.artemwhite.shared.model.enums.SatelliteSubcategory

class InvalidCategoryException
    (
    val category: ObjectCategory,
    val subcategory: SatelliteSubcategory?
) : DomainException("Subcategory $subcategory is only allowed for SATELLITE, got $category") {
}
