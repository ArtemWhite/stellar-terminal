package com.artemwhite.satellite.adapter.outbound.persistence.adapters

import com.artemwhite.satellite.adapter.outbound.persistence.mappers.toDomain
import com.artemwhite.satellite.adapter.outbound.persistence.mappers.toEntity
import com.artemwhite.satellite.adapter.outbound.persistence.repositories.OrbitSnapshotJpaRepository
import com.artemwhite.satellite.adapter.outbound.persistence.repositories.SatelliteJpaRepository
import com.artemwhite.satellite.adapter.outbound.persistence.repositories.TleRecordJpaRepository
import com.artemwhite.satellite.domain.models.OrbitSnapshot
import com.artemwhite.satellite.domain.models.PageResult
import com.artemwhite.satellite.domain.models.Satellite
import com.artemwhite.satellite.domain.models.TleRecord
import com.artemwhite.satellite.domain.ports.outbound.SatelliteRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class SatelliteRepositoryAdapter(
    private val satelliteJpaRepository: SatelliteJpaRepository,
    private val tleRecordJpaRepository: TleRecordJpaRepository,
    private val orbitSnapshotJpaRepository: OrbitSnapshotJpaRepository,
) : SatelliteRepository {

    override fun findSatellite(objectId: UUID): Satellite? =
        satelliteJpaRepository.findById(objectId).map { it.toDomain() }.orElse(null)

    override fun findAll(): List<Satellite> =
        satelliteJpaRepository.findAll().map { it.toDomain() }

    override fun saveSatellite(satellite: Satellite): Satellite =
        satelliteJpaRepository.save(satellite.toEntity()).toDomain()

    override fun findTle(objectId: UUID): TleRecord? =
        tleRecordJpaRepository.findById(objectId).map { it.toDomain() }.orElse(null)

    override fun saveTle(objectId: UUID, tle: TleRecord): TleRecord =
        tleRecordJpaRepository.save(tle.toEntity(objectId)).toDomain()

    override fun findSnapshots(objectId: UUID, page: Int, size: Int): PageResult<OrbitSnapshot> {
        val result = orbitSnapshotJpaRepository.findByObjectIdOrderByCalculatedAtAsc(
            objectId,
            PageRequest.of(page, size),
        )
        return PageResult(
            items = result.content.map { it.toDomain() },
            page = page,
            size = size,
            total = result.totalElements,
        )
    }

    override fun saveSnapshot(objectId: UUID, snapshot: OrbitSnapshot): OrbitSnapshot =
        orbitSnapshotJpaRepository.save(snapshot.toEntity(objectId)).toDomain()
}
