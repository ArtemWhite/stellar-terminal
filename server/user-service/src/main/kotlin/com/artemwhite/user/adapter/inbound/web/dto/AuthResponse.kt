package com.artemwhite.user.adapter.inbound.web.dto

data class AuthResponse(
    val user: UserResponse,
    val token: String,
)
