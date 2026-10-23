package com.artemwhite.satellite.adapter.outbound.persistence.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "orbit_snapshots")
class OrbitSnapshotEntity(
    @Id
    val id: UUID,

    @Column(nullable = false)
    var objectId: UUID,

    @Column(nullable = false)
    var x: Double,

    @Column(nullable = false)
    var y: Double,

    @Column(nullable = false)
    var z: Double,

    @Column(nullable = false)
    var vx: Double,

    @Column(nullable = false)
    var vy: Double,

    @Column(nullable = false)
    var vz: Double,

    @Column(nullable = false)
    var calculatedAt: Instant,
)
