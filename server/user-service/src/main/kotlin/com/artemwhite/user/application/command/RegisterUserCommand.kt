package com.artemwhite.user.application.command

import com.artemwhite.user.domain.models.Email

data class RegisterUserCommand(
    val nickname: String,
    val email: Email,
    val password: String,
)
