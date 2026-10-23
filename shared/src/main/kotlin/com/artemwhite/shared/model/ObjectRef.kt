package com.artemwhite.shared.model

import com.artemwhite.shared.model.enums.ObjectCategory
import java.util.UUID

data class ObjectRef(
    val objectId: UUID,
    val name: String,
    val category: ObjectCategory,
)
