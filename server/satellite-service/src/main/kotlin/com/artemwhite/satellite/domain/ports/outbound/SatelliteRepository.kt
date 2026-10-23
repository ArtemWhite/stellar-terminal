package com.artemwhite.satellite.domain.ports.outbound

import com.artemwhite.satellite.domain.models.OrbitSnapshot
import com.artemwhite.satellite.domain.models.PageResult
import com.artemwhite.satellite.domain.models.Satellite
import com.artemwhite.satellite.domain.models.TleRecord
import java.util.UUID

interface SatelliteRepository {
    fun findSatellite(objectId: UUID): Satellite?
    fun findAll(): List<Satellite>
    fun saveSatellite(satellite: Satellite): Satellite

    fun findTle(objectId: UUID): TleRecord?
    fun saveTle(objectId: UUID, tle: TleRecord): TleRecord

    fun findSnapshots(objectId: UUID, page: Int, size: Int): PageResult<OrbitSnapshot>
    fun saveSnapshot(objectId: UUID, snapshot: OrbitSnapshot): OrbitSnapshot
}
