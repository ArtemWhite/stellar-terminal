package com.artemwhite.satellite.adapter.outbound.persistence.entities

import com.artemwhite.shared.model.enums.ObjectStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "satellites")
class SatelliteEntity(
    @Id
    val objectId: UUID,

    @Column(nullable = false, unique = true)
    var noradId: Int,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ObjectStatus,
)
