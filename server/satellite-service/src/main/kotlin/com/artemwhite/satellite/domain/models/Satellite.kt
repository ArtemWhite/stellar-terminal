package com.artemwhite.satellite.domain.models

import com.artemwhite.shared.model.enums.ObjectStatus
import java.util.UUID

class Satellite(
    val objectId: UUID,
    val noradId: Int,
    status: ObjectStatus,
) {
    var status: ObjectStatus = status
        private set

    init {
        require(noradId > 0) { "NORAD id must be positive, got $noradId" }
    }

    fun changeStatus(newStatus: ObjectStatus) {
        status = newStatus
    }

    companion object {
        fun create(objectId: UUID, noradId: Int): Satellite =
            Satellite(objectId = objectId, noradId = noradId, status = ObjectStatus.ACTIVE)
    }
}
