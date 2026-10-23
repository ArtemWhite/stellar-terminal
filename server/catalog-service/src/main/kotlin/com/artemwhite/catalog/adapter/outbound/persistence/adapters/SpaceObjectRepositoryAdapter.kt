package com.artemwhite.catalog.adapter.outbound.persistence.adapters

import com.artemwhite.catalog.adapter.outbound.persistence.mappers.toDomain
import com.artemwhite.catalog.adapter.outbound.persistence.mappers.toEntity
import com.artemwhite.catalog.adapter.outbound.persistence.repositories.SpaceObjectJpaRepository
import com.artemwhite.catalog.adapter.outbound.persistence.repositories.SpaceObjectSpecifications
import com.artemwhite.catalog.domain.models.PageResult
import com.artemwhite.catalog.domain.models.SearchFilter
import com.artemwhite.catalog.domain.models.SpaceObject
import com.artemwhite.catalog.domain.ports.outbound.SpaceObjectRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class SpaceObjectRepositoryAdapter(
    private val jpaRepository: SpaceObjectJpaRepository,
) : SpaceObjectRepository {

    override fun findById(id: UUID): SpaceObject? =
        jpaRepository.findById(id).map { it.toDomain() }.orElse(null)

    override fun existsById(id: UUID): Boolean = jpaRepository.existsById(id)

    override fun search(filter: SearchFilter): PageResult<SpaceObject> {
        val page = jpaRepository.findAll(
            SpaceObjectSpecifications.from(filter),
            PageRequest.of(filter.page, filter.size),
        )
        return PageResult(
            items = page.content.map { it.toDomain() },
            page = filter.page,
            size = filter.size,
            total = page.totalElements,
        )
    }

    override fun save(spaceObject: SpaceObject): SpaceObject =
        jpaRepository.save(spaceObject.toEntity()).toDomain()

    override fun deleteById(id: UUID) {
        jpaRepository.deleteById(id)
    }
}
