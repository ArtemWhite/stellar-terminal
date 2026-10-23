package com.artemwhite.satellite.adapter.outbound.persistence.mappers

import com.artemwhite.satellite.adapter.outbound.persistence.entities.OrbitSnapshotEntity
import com.artemwhite.satellite.adapter.outbound.persistence.entities.SatelliteEntity
import com.artemwhite.satellite.adapter.outbound.persistence.entities.TleRecordEntity
import com.artemwhite.satellite.domain.models.OrbitSnapshot
import com.artemwhite.satellite.domain.models.Position
import com.artemwhite.satellite.domain.models.Satellite
import com.artemwhite.satellite.domain.models.TleRecord
import java.util.UUID

fun SatelliteEntity.toDomain(): Satellite = Satellite(
    objectId = objectId,
    noradId = noradId,
    status = status,
)

fun Satellite.toEntity(): SatelliteEntity = SatelliteEntity(
    objectId = objectId,
    noradId = noradId,
    status = status,
)

fun TleRecordEntity.toDomain(): TleRecord = TleRecord(
    line1 = line1,
    line2 = line2,
    epoch = epoch,
    fetchedAt = fetchedAt,
)

fun TleRecord.toEntity(objectId: UUID): TleRecordEntity = TleRecordEntity(
    objectId = objectId,
    line1 = line1,
    line2 = line2,
    epoch = epoch,
    fetchedAt = fetchedAt,
)

fun OrbitSnapshotEntity.toDomain(): OrbitSnapshot = OrbitSnapshot(
    position = Position(x = x, y = y, z = z, vx = vx, vy = vy, vz = vz),
    calculatedAt = calculatedAt,
)

fun OrbitSnapshot.toEntity(objectId: UUID): OrbitSnapshotEntity = OrbitSnapshotEntity(
    id = UUID.randomUUID(),
    objectId = objectId,
    x = position.x,
    y = position.y,
    z = position.z,
    vx = position.vx,
    vy = position.vy,
    vz = position.vz,
    calculatedAt = calculatedAt,
)
