package com.artemwhite.user.adapter.inbound.web

import com.artemwhite.user.adapter.inbound.web.dto.SatelliteEventRequest
import com.artemwhite.user.adapter.inbound.web.mappers.toDomainEvent
import com.artemwhite.user.application.usecase.HandleSatelliteEventUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/events")
class EventController(
    private val handleEvent: HandleSatelliteEventUseCase,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun handle(@Valid @RequestBody request: SatelliteEventRequest) {
        handleEvent.execute(request.toDomainEvent())
    }
}
