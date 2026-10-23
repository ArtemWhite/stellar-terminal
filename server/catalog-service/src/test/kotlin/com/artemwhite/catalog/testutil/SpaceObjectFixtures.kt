package com.artemwhite.catalog.testutil

import com.artemwhite.catalog.domain.models.SpaceObject
import com.artemwhite.shared.model.enums.ObjectCategory
import com.artemwhite.shared.model.enums.ObjectStatus
import com.artemwhite.shared.model.enums.SatelliteSubcategory
import com.artemwhite.shared.model.enums.SourceType
import java.time.Instant
import java.util.UUID

object SpaceObjectFixtures {

    val NOW: Instant = Instant.parse("2026-01-01T00:00:00Z")
    val LATER: Instant = Instant.parse("2026-01-02T00:00:00Z")

    fun satellite(
        id: UUID = UUID.randomUUID(),
        status: ObjectStatus = ObjectStatus.ACTIVE,
    ): SpaceObject = SpaceObject(
        id = id,
        name = "ISS",
        category = ObjectCategory.SATELLITE,
        subcategory = SatelliteSubcategory.SCIENTIFIC,
        description = "International Space Station",
        sourceId = "25544",
        sourceType = SourceType.CELESTRAK,
        status = status,
        createdAt = NOW,
        updatedAt = NOW,
    )
}
