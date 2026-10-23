package com.artemwhite.satellite.adapter.outbound.persistence.repositories

import com.artemwhite.satellite.adapter.outbound.persistence.entities.OrbitSnapshotEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OrbitSnapshotJpaRepository : JpaRepository<OrbitSnapshotEntity, UUID> {
    fun findByObjectIdOrderByCalculatedAtAsc(objectId: UUID, pageable: Pageable): Page<OrbitSnapshotEntity>
}
