package com.artemwhite.user.adapter.outbound.persistence.adapters

import com.artemwhite.user.adapter.outbound.persistence.mappers.toDomain
import com.artemwhite.user.adapter.outbound.persistence.mappers.toEntity
import com.artemwhite.user.adapter.outbound.persistence.repositories.UserJpaRepository
import com.artemwhite.user.domain.models.Email
import com.artemwhite.user.domain.models.User
import com.artemwhite.user.domain.ports.outbound.UserRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserRepositoryAdapter(
    private val jpaRepository: UserJpaRepository,
) : UserRepository {

    override fun findById(id: UUID): User? =
        jpaRepository.findById(id).map { it.toDomain() }.orElse(null)

    override fun findByEmail(email: Email): User? =
        jpaRepository.findByEmail(email.value)?.toDomain()

    override fun existsByEmail(email: Email): Boolean = jpaRepository.existsByEmail(email.value)

    override fun save(user: User): User = jpaRepository.save(user.toEntity()).toDomain()
}
