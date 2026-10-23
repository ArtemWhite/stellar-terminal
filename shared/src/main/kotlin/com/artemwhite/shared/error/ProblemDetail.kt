package com.artemwhite.shared.error

data class ProblemDetail(
    val type: String,
    val title: String,
    val status: Int,
    val detail: String,
)
