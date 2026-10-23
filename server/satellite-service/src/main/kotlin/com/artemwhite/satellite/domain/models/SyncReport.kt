package com.artemwhite.satellite.domain.models

data class SyncReport(
    val updated: Int,
    val failed: Int,
    val decayed: Int,
)
