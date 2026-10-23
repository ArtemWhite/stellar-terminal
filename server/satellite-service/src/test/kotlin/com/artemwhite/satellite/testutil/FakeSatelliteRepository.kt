package com.artemwhite.satellite.testutil

import com.artemwhite.satellite.domain.models.OrbitSnapshot
import com.artemwhite.satellite.domain.models.PageResult
import com.artemwhite.satellite.domain.models.Satellite
import com.artemwhite.satellite.domain.models.TleRecord
import com.artemwhite.satellite.domain.ports.outbound.SatelliteRepository
import java.util.UUID

/**
 * Hand-rolled in-memory fake of the repository port. Used in unit tests so the
 * application layer can be tested without a database or a mocking library.
 */
class FakeSatelliteRepository : SatelliteRepository {

    val satellites = LinkedHashMap<UUID, Satellite>()
    val tles = LinkedHashMap<UUID, TleRecord>()
    val snapshots = LinkedHashMap<UUID, MutableList<OrbitSnapshot>>()

    override fun findSatellite(objectId: UUID): Satellite? = satellites[objectId]

    override fun findAll(): List<Satellite> = satellites.values.toList()

    override fun saveSatellite(satellite: Satellite): Satellite {
        satellites[satellite.objectId] = satellite
        return satellite
    }

    override fun findTle(objectId: UUID): TleRecord? = tles[objectId]

    override fun saveTle(objectId: UUID, tle: TleRecord): TleRecord {
        tles[objectId] = tle
        return tle
    }

    override fun findSnapshots(objectId: UUID, page: Int, size: Int): PageResult<OrbitSnapshot> {
        val all = snapshots[objectId]?.toList() ?: emptyList()
        val from = page * size
        return PageResult(
            items = all.drop(from).take(size),
            page = page,
            size = size,
            total = all.size.toLong(),
        )
    }

    override fun saveSnapshot(objectId: UUID, snapshot: OrbitSnapshot): OrbitSnapshot {
        snapshots.getOrPut(objectId) { mutableListOf() }.add(snapshot)
        return snapshot
    }
}
