package com.artemwhite.satellite.adapter.inbound.web.mappers

import com.artemwhite.satellite.adapter.inbound.web.dto.OrbitSnapshotResponse
import com.artemwhite.satellite.adapter.inbound.web.dto.PassPredictionResponse
import com.artemwhite.satellite.adapter.inbound.web.dto.PositionResponse
import com.artemwhite.satellite.adapter.inbound.web.dto.RegisterSatelliteRequest
import com.artemwhite.satellite.adapter.inbound.web.dto.SatelliteResponse
import com.artemwhite.satellite.adapter.inbound.web.dto.SyncReportResponse
import com.artemwhite.satellite.application.command.RegisterSatelliteCommand
import com.artemwhite.satellite.domain.models.OrbitSnapshot
import com.artemwhite.satellite.domain.models.PageResult
import com.artemwhite.satellite.domain.models.PassPrediction
import com.artemwhite.satellite.domain.models.Position
import com.artemwhite.satellite.domain.models.Satellite
import com.artemwhite.satellite.domain.models.SyncReport

fun RegisterSatelliteRequest.toCommand(): RegisterSatelliteCommand = RegisterSatelliteCommand(
    objectId = objectId,
    noradId = noradId,
)

fun Position.toResponse(): PositionResponse = PositionResponse(
    x = x,
    y = y,
    z = z,
    vx = vx,
    vy = vy,
    vz = vz,
)

fun PassPrediction.toResponse(): PassPredictionResponse = PassPredictionResponse(
    objectId = objectId,
    start = start,
    end = end,
    maxElevation = maxElevation,
)

fun OrbitSnapshot.toResponse(): OrbitSnapshotResponse = OrbitSnapshotResponse(
    position = position.toResponse(),
    calculatedAt = calculatedAt,
)

fun PageResult<OrbitSnapshot>.toResponse(): PageResult<OrbitSnapshotResponse> = PageResult(
    items = items.map { it.toResponse() },
    page = page,
    size = size,
    total = total,
)

fun Satellite.toResponse(): SatelliteResponse = SatelliteResponse(
    objectId = objectId,
    noradId = noradId,
    status = status,
)

fun SyncReport.toResponse(): SyncReportResponse = SyncReportResponse(
    updated = updated,
    failed = failed,
    decayed = decayed,
)
