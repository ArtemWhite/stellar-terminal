package com.artemwhite.satellite.domain.models

data class ActiveTle(
    val noradId: Int,
    val tle: TleRecord,
)
