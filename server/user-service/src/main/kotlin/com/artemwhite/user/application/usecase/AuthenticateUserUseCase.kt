package com.artemwhite.user.application.usecase

import com.artemwhite.user.domain.exceptions.InvalidCredentialsException
import com.artemwhite.user.domain.models.Email
import com.artemwhite.user.domain.models.User
import com.artemwhite.user.domain.ports.outbound.PasswordHasher
import com.artemwhite.user.domain.ports.outbound.TokenProvider
import com.artemwhite.user.domain.ports.outbound.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

data class AuthResult(val user: User, val token: String)

@Service
class AuthenticateUserUseCase(
    private val userRepository: UserRepository,
    private val passwordHasher: PasswordHasher,
    private val tokenProvider: TokenProvider,
) {
    private val log = LoggerFactory.getLogger(AuthenticateUserUseCase::class.java)

    fun execute(email: Email, rawPassword: String): AuthResult {
        val user = userRepository.findByEmail(email) ?: throw InvalidCredentialsException()
        if (!passwordHasher.verify(rawPassword, user.passwordHash)) {
            throw InvalidCredentialsException()
        }
        val token = tokenProvider.issue(user.id, user.role)
        log.info("Authenticated user {}", user.id)
        return AuthResult(user = user, token = token)
    }
}
