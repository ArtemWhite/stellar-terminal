package com.artemwhite.catalog.domain.models

import com.artemwhite.shared.model.enums.ObjectCategory
import com.artemwhite.shared.model.enums.ObjectStatus

data class SearchFilter
    (
    val category: ObjectCategory? = null,
    val status: ObjectStatus? = null,
    val query: String? = null,
    val page: Int = 0,
    val size: Int = 20
)