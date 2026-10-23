package com.artemwhite.satellite.adapter.inbound.web

import com.artemwhite.satellite.adapter.inbound.web.dto.RegisterSatelliteRequest
import com.artemwhite.satellite.adapter.inbound.web.dto.SatelliteResponse
import com.artemwhite.satellite.adapter.inbound.web.dto.SyncReportResponse
import com.artemwhite.satellite.adapter.inbound.web.mappers.toCommand
import com.artemwhite.satellite.adapter.inbound.web.mappers.toResponse
import com.artemwhite.satellite.application.usecase.RegisterSatelliteUseCase
import com.artemwhite.satellite.application.usecase.SyncSatellitesUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin")
class AdminController(
    private val syncSatellites: SyncSatellitesUseCase,
    private val registerSatellite: RegisterSatelliteUseCase,
) {

    @PostMapping("/sync")
    fun sync(): SyncReportResponse = syncSatellites.sync().toResponse()

    @PostMapping("/satellites")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@Valid @RequestBody request: RegisterSatelliteRequest): SatelliteResponse =
        registerSatellite.execute(request.toCommand()).toResponse()
}
