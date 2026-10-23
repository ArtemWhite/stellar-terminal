package com.artemwhite.user.adapter.outbound.persistence.adapters

import com.artemwhite.user.adapter.outbound.persistence.mappers.toDomain
import com.artemwhite.user.adapter.outbound.persistence.mappers.toEntity
import com.artemwhite.user.adapter.outbound.persistence.repositories.CollectionJpaRepository
import com.artemwhite.user.domain.models.Collection
import com.artemwhite.user.domain.models.PageResult
import com.artemwhite.user.domain.ports.outbound.CollectionRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CollectionRepositoryAdapter(
    private val jpaRepository: CollectionJpaRepository,
) : CollectionRepository {

    override fun findById(id: UUID): Collection? =
        jpaRepository.findById(id).map { it.toDomain() }.orElse(null)

    override fun findByUser(userId: UUID, page: Int, size: Int): PageResult<Collection> {
        val result = jpaRepository.findByUserIdOrderByNameAsc(userId, PageRequest.of(page, size))
        return PageResult(
            items = result.content.map { it.toDomain() },
            page = page,
            size = size,
            total = result.totalElements,
        )
    }

    override fun save(collection: Collection): Collection =
        jpaRepository.save(collection.toEntity()).toDomain()

    override fun delete(id: UUID) {
        jpaRepository.deleteById(id)
    }
}
