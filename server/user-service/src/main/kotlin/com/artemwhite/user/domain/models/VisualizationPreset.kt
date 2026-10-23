package com.artemwhite.user.domain.models

import java.util.UUID

class VisualizationPreset(
    val id: UUID,
    val userId: UUID,
    colors: String,
    scale: Double,
) {
    var colors: String = colors
        private set

    var scale: Double = scale
        private set

    init {
        require(this.scale > 0) { "scale must be positive, got $scale" }
    }

    fun update(newColors: String, newScale: Double) {
        require(newScale > 0) { "scale must be positive, got $newScale" }
        colors = newColors
        scale = newScale
    }

    companion object {
        fun create(id: UUID, userId: UUID, colors: String, scale: Double): VisualizationPreset =
            VisualizationPreset(id = id, userId = userId, colors = colors, scale = scale)
    }
}
