package com.artemwhite.satellite.domain.models

data class PageResult<T>(
    val items: List<T>,
    val page: Int,
    val size: Int,
    val total: Long,
)
