package com.artemwhite.user.adapter.outbound.persistence.adapters

import com.artemwhite.user.adapter.outbound.persistence.mappers.toDomain
import com.artemwhite.user.adapter.outbound.persistence.mappers.toEntity
import com.artemwhite.user.adapter.outbound.persistence.repositories.FavoriteJpaRepository
import com.artemwhite.user.domain.models.Favorite
import com.artemwhite.user.domain.models.PageResult
import com.artemwhite.user.domain.ports.outbound.FavoriteRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class FavoriteRepositoryAdapter(
    private val jpaRepository: FavoriteJpaRepository,
) : FavoriteRepository {

    override fun findByUser(userId: UUID, page: Int, size: Int): PageResult<Favorite> {
        val result = jpaRepository.findByUserIdOrderByAddedAtDesc(userId, PageRequest.of(page, size))
        return PageResult(
            items = result.content.map { it.toDomain() },
            page = page,
            size = size,
            total = result.totalElements,
        )
    }

    override fun findUserIdsByObject(objectId: UUID): List<UUID> =
        jpaRepository.findByObjectId(objectId).map { it.userId }

    override fun exists(userId: UUID, objectId: UUID): Boolean =
        jpaRepository.existsByUserIdAndObjectId(userId, objectId)

    override fun save(favorite: Favorite): Favorite = jpaRepository.save(favorite.toEntity()).toDomain()

    override fun delete(userId: UUID, objectId: UUID) {
        jpaRepository.deleteByUserIdAndObjectId(userId, objectId)
    }
}
