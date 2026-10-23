package com.artemwhite.satellite.adapter.outbound.persistence.repositories

import com.artemwhite.satellite.adapter.outbound.persistence.entities.SatelliteEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SatelliteJpaRepository : JpaRepository<SatelliteEntity, UUID>
