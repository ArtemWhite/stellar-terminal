package com.artemwhite.satellite.domain.exceptions

import com.artemwhite.shared.model.enums.ObjectCategory
import java.util.UUID

class NotASatelliteException(val objectId: UUID, val category: ObjectCategory) :
    DomainException("Object $objectId is $category, not a satellite")
