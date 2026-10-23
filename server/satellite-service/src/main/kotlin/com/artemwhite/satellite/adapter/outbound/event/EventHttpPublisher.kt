package com.artemwhite.satellite.adapter.outbound.event

import com.artemwhite.satellite.domain.ports.outbound.EventPublisher
import com.artemwhite.shared.model.SatelliteEvent
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class EventHttpPublisher(
    @Value("\${satellite.event.url}") private val url: String,
) : EventPublisher {
    private val log = LoggerFactory.getLogger(EventHttpPublisher::class.java)
    private val client: RestClient = RestClient.builder().build()

    override fun publish(event: SatelliteEvent) {
        log.info("Publishing {} event for object {}", event.type, event.objectId)
        client.post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(event)
            .retrieve()
            .toBodilessEntity()
    }
}
