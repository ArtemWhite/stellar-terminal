package com.artemwhite.satellite.adapter.inbound.web.dto

data class SyncReportResponse(
    val updated: Int,
    val failed: Int,
    val decayed: Int,
)
