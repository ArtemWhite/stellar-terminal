package com.artemwhite.catalog.adapter.outbound.persistence.entities

import com.artemwhite.shared.model.enums.ObjectCategory
import com.artemwhite.shared.model.enums.ObjectStatus
import com.artemwhite.shared.model.enums.SatelliteSubcategory
import com.artemwhite.shared.model.enums.SourceType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "space_objects")
class SpaceObjectEntity(
    @Id
    val id: UUID,

    @Column(nullable = false)
    var name: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var category: ObjectCategory,

    @Enumerated(EnumType.STRING)
    var subcategory: SatelliteSubcategory?,

    @Column(nullable = false)
    var description: String,

    @Column(nullable = false)
    var sourceId: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var sourceType: SourceType,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ObjectStatus,

    @Column(nullable = false)
    var createdAt: Instant,

    @Column(nullable = false)
    var updatedAt: Instant,
)
