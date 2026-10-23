package com.artemwhite.user.adapter.outbound.persistence.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "presets")
class VisualizationPresetEntity(
    @Id
    val id: UUID,

    @Column(name = "user_id", nullable = false)
    var userId: UUID,

    @Column(nullable = false)
    var colors: String,

    @Column(nullable = false)
    var scale: Double,
)
