package com.artemwhite.catalog.domain.models

import com.artemwhite.catalog.domain.exceptions.InvalidCategoryException
import com.artemwhite.shared.model.enums.ObjectCategory
import com.artemwhite.shared.model.enums.ObjectStatus
import com.artemwhite.shared.model.enums.SatelliteSubcategory
import com.artemwhite.shared.model.enums.SourceType
import java.time.Instant
import java.util.UUID

class SpaceObject(
    val id: UUID,
    val name: String,
    category: ObjectCategory,
    subcategory: SatelliteSubcategory?,
    val description: String,
    val sourceId: String,
    val sourceType: SourceType,
    status: ObjectStatus,
    val createdAt: Instant,
    updatedAt: Instant,
) {
    var category: ObjectCategory = category
        private set

    var subcategory: SatelliteSubcategory? = subcategory
        private set

    var status: ObjectStatus = status
        private set

    var updatedAt: Instant = updatedAt
        private set

    init {
        requireValidSubcategory(this.category, this.subcategory)
    }

    fun changeStatus(newStatus: ObjectStatus, now: Instant) {
        status = newStatus
        updatedAt = now
    }

    fun recategorize(newCategory: ObjectCategory, newSubcategory: SatelliteSubcategory?, now: Instant) {
        requireValidSubcategory(newCategory, newSubcategory)
        category = newCategory
        subcategory = if (newCategory == ObjectCategory.SATELLITE) newSubcategory else null
        updatedAt = now
    }

    companion object {
        fun create(
            id: UUID,
            name: String,
            category: ObjectCategory,
            subcategory: SatelliteSubcategory?,
            description: String,
            sourceId: String,
            sourceType: SourceType,
            now: Instant,
        ): SpaceObject = SpaceObject(
            id = id,
            name = name,
            category = category,
            subcategory = subcategory,
            description = description,
            sourceId = sourceId,
            sourceType = sourceType,
            status = ObjectStatus.ACTIVE,
            createdAt = now,
            updatedAt = now,
        )
    }
}

private fun requireValidSubcategory(category: ObjectCategory, subcategory: SatelliteSubcategory?) {
    if (category != ObjectCategory.SATELLITE && subcategory != null) {
        throw InvalidCategoryException(category, subcategory)
    }
}
