package com.artemwhite.user.adapter.inbound.web.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterUserRequest(
    @field:NotBlank val nickname: String,
    @field:Email @field:NotBlank val email: String,
    @field:Size(min = 6, max = 72) val password: String,
)
