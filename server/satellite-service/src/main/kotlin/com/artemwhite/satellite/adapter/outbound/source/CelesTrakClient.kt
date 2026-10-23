package com.artemwhite.satellite.adapter.outbound.source

import com.artemwhite.satellite.adapter.outbound.calculation.TleParser
import com.artemwhite.satellite.domain.models.ActiveTle
import com.artemwhite.satellite.domain.ports.outbound.Clock
import com.artemwhite.satellite.domain.ports.outbound.TleProvider
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.time.Instant

@Component
class CelesTrakClient(
    private val parser: TleParser,
    private val clock: Clock,
    @Value("\${satellite.celestrak.url}") private val url: String,
) : TleProvider {
    private val log = LoggerFactory.getLogger(CelesTrakClient::class.java)
    private val client: RestClient = RestClient.builder().build()

    override fun fetchActiveSatellites(): List<ActiveTle> {
        val body = client.get()
            .uri(url)
            .retrieve()
            .body(String::class.java)
            ?: throw IllegalStateException("CelesTrak returned an empty response")
        val activeTles = parseResponse(body, clock.now())
        log.info("Fetched {} active TLE records from CelesTrak", activeTles.size)
        return activeTles
    }

    fun parseResponse(body: String, fetchedAt: Instant): List<ActiveTle> {
        val lines = body.lines().map { it.trim() }.filter { it.isNotEmpty() }
        val result = mutableListOf<ActiveTle>()
        var i = 0
        while (i < lines.size) {
            if (lines[i].startsWith("1 ") && i + 1 < lines.size && lines[i + 1].startsWith("2 ")) {
                result += parser.toActiveTle(lines[i], lines[i + 1], fetchedAt)
                i += 2
            } else {
                i += 1
            }
        }
        return result
    }
}
