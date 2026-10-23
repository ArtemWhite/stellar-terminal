package com.artemwhite.user.application.usecase

import com.artemwhite.user.application.command.RegisterUserCommand
import com.artemwhite.user.domain.exceptions.DuplicateEmailException
import com.artemwhite.user.domain.models.Role
import com.artemwhite.user.domain.models.User
import com.artemwhite.user.domain.ports.outbound.Clock
import com.artemwhite.user.domain.ports.outbound.PasswordHasher
import com.artemwhite.user.domain.ports.outbound.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class RegisterUserUseCase(
    private val userRepository: UserRepository,
    private val passwordHasher: PasswordHasher,
    private val clock: Clock,
) {
    private val log = LoggerFactory.getLogger(RegisterUserUseCase::class.java)

    fun execute(command: RegisterUserCommand): User {
        if (userRepository.existsByEmail(command.email)) {
            throw DuplicateEmailException(command.email.value)
        }
        val user = userRepository.save(
            User.create(
                id = UUID.randomUUID(),
                nickname = command.nickname,
                email = command.email,
                passwordHash = passwordHasher.hash(command.password),
                role = Role.REGISTERED,
                now = clock.now(),
            ),
        )
        log.info("Registered user {} ({})", user.id, user.nickname)
        return user
    }
}
