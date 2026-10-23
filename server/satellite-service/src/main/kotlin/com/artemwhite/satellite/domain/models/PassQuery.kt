package com.artemwhite.satellite.domain.models

data class PassQuery(
    val minElevationDeg: Double,
    val windowSeconds: Long,
    val stepSeconds: Long,
) {
    init {
        require(minElevationDeg in -90.0..90.0) { "min elevation must be within [-90, 90]" }
        require(windowSeconds > 0) { "window must be positive, got $windowSeconds" }
        require(stepSeconds > 0) { "step must be positive, got $stepSeconds" }
        require(stepSeconds <= windowSeconds) { "step must not exceed the window" }
    }

    companion object {
        fun builder(): Builder = Builder()
    }

    class Builder {
        private var minElevationDeg: Double = 10.0
        private var windowSeconds: Long = 86_400L
        private var stepSeconds: Long = 60L

        fun minElevationDeg(value: Double): Builder = apply { minElevationDeg = value }
        fun windowSeconds(value: Long): Builder = apply { windowSeconds = value }
        fun stepSeconds(value: Long): Builder = apply { stepSeconds = value }

        fun build(): PassQuery = PassQuery(
            minElevationDeg = minElevationDeg,
            windowSeconds = windowSeconds,
            stepSeconds = stepSeconds,
        )
    }
}
