package com.artemwhite.satellite.adapter.inbound.web.dto

import com.artemwhite.shared.model.enums.ObjectStatus
import java.util.UUID

data class SatelliteResponse(
    val objectId: UUID,
    val noradId: Int,
    val status: ObjectStatus,
)
