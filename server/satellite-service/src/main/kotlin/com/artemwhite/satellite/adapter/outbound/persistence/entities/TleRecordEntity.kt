package com.artemwhite.satellite.adapter.outbound.persistence.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "tle_records")
class TleRecordEntity(
    @Id
    val objectId: UUID,

    @Column(nullable = false, length = 80)
    var line1: String,

    @Column(nullable = false, length = 80)
    var line2: String,

    @Column(nullable = false)
    var epoch: Instant,

    @Column(nullable = false)
    var fetchedAt: Instant,
)
