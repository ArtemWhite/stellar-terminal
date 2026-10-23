package com.artemwhite.catalog.adapter.outbound.persistence.repositories

import com.artemwhite.catalog.adapter.outbound.persistence.entities.SpaceObjectEntity
import com.artemwhite.catalog.domain.models.SearchFilter
import com.artemwhite.shared.model.enums.ObjectCategory
import com.artemwhite.shared.model.enums.ObjectStatus
import org.springframework.data.jpa.domain.Specification

object SpaceObjectSpecifications {

    fun from(filter: SearchFilter): Specification<SpaceObjectEntity> {
        var spec: Specification<SpaceObjectEntity> =
            Specification { _, _, cb -> cb.conjunction() }
        filter.category?.let { spec = spec.and(categoryEq(it)) }
        filter.status?.let { spec = spec.and(statusEq(it)) }
        filter.query?.takeIf { it.isNotBlank() }?.let { spec = spec.and(nameContains(it)) }
        return spec
    }

    private fun categoryEq(category: ObjectCategory): Specification<SpaceObjectEntity> =
        Specification { root, _, cb -> cb.equal(root.get<ObjectCategory>("category"), category) }

    private fun statusEq(status: ObjectStatus): Specification<SpaceObjectEntity> =
        Specification { root, _, cb -> cb.equal(root.get<ObjectStatus>("status"), status) }

    private fun nameContains(query: String): Specification<SpaceObjectEntity> =
        Specification { root, _, cb ->
            cb.like(
                cb.lower(root.get<String>("name")),
                "%${escapeLike(query).lowercase()}%",
                '\\',
            )
        }
}

private fun escapeLike(value: String): String =
    value
        .replace("\\", "\\\\")
        .replace("%", "\\%")
        .replace("_", "\\_")
