package com.artemwhite.user.adapter.inbound.web

import com.artemwhite.user.adapter.inbound.web.dto.AuthResponse
import com.artemwhite.user.adapter.inbound.web.dto.LoginRequest
import com.artemwhite.user.adapter.inbound.web.dto.RegisterUserRequest
import com.artemwhite.user.adapter.inbound.web.dto.UserResponse
import com.artemwhite.user.adapter.inbound.web.mappers.toCommand
import com.artemwhite.user.adapter.inbound.web.mappers.toResponse
import com.artemwhite.user.application.usecase.AuthenticateUserUseCase
import com.artemwhite.user.application.usecase.RegisterUserUseCase
import com.artemwhite.user.domain.models.Email
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val registerUser: RegisterUserUseCase,
    private val authenticateUser: AuthenticateUserUseCase,
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@Valid @RequestBody request: RegisterUserRequest): UserResponse =
        registerUser.execute(request.toCommand()).toResponse()

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): AuthResponse =
        authenticateUser.execute(Email(request.email), request.password).toResponse()
}
