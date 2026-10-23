package com.artemwhite.user.application.usecase

import com.artemwhite.user.domain.exceptions.UserNotFoundException
import com.artemwhite.user.domain.models.User
import com.artemwhite.user.domain.ports.outbound.UserRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetUserUseCase(
    private val userRepository: UserRepository,
) {
    fun execute(id: UUID): User = userRepository.findById(id) ?: throw UserNotFoundException(id)
}
