package com.artemwhite.satellite.adapter.inbound.web

import com.artemwhite.satellite.adapter.inbound.web.dto.OrbitSnapshotResponse
import com.artemwhite.satellite.adapter.inbound.web.dto.PassPredictionResponse
import com.artemwhite.satellite.adapter.inbound.web.dto.PositionResponse
import com.artemwhite.satellite.adapter.inbound.web.mappers.toResponse
import com.artemwhite.satellite.application.usecase.GetPositionUseCase
import com.artemwhite.satellite.application.usecase.GetTimelineUseCase
import com.artemwhite.satellite.application.usecase.PredictPassUseCase
import com.artemwhite.satellite.domain.models.PageResult
import com.artemwhite.satellite.domain.models.PassQuery
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.util.UUID

@RestController
@RequestMapping("/api/v1/satellites")
class SatelliteController(
    private val getPosition: GetPositionUseCase,
    private val predictPass: PredictPassUseCase,
    private val getTimeline: GetTimelineUseCase,
) {

    @GetMapping("/{objectId}/position")
    fun position(
        @PathVariable objectId: UUID,
        @RequestParam(required = false) at: Instant?,
    ): PositionResponse = getPosition.execute(objectId, at).toResponse()

    @GetMapping("/{objectId}/passes")
    fun passes(
        @PathVariable objectId: UUID,
        @RequestParam(defaultValue = "10.0") minElevation: Double,
        @RequestParam(defaultValue = "86400") window: Long,
        @RequestParam(defaultValue = "60") step: Long,
    ): List<PassPredictionResponse> = predictPass.execute(
        objectId,
        PassQuery.builder()
            .minElevationDeg(minElevation)
            .windowSeconds(window)
            .stepSeconds(step)
            .build(),
    ).map { it.toResponse() }

    @GetMapping("/{objectId}/timeline")
    fun timeline(
        @PathVariable objectId: UUID,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
    ): PageResult<OrbitSnapshotResponse> =
        getTimeline.execute(
            objectId,
            page = page.coerceAtLeast(0),
            size = size.coerceIn(1, MAX_PAGE_SIZE),
        ).toResponse()

    companion object {
        private const val MAX_PAGE_SIZE = 100
    }
}
