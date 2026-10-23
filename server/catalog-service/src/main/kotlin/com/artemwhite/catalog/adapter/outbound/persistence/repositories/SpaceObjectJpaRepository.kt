package com.artemwhite.catalog.adapter.outbound.persistence.repositories

import com.artemwhite.catalog.adapter.outbound.persistence.entities.SpaceObjectEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.UUID

interface SpaceObjectJpaRepository :
    JpaRepository<SpaceObjectEntity, UUID>,
    JpaSpecificationExecutor<SpaceObjectEntity>
