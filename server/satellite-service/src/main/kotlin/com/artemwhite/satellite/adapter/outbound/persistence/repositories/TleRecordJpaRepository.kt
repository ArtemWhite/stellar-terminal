package com.artemwhite.satellite.adapter.outbound.persistence.repositories

import com.artemwhite.satellite.adapter.outbound.persistence.entities.TleRecordEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TleRecordJpaRepository : JpaRepository<TleRecordEntity, UUID>
