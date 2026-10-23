package com.artemwhite.satellite.integration

import com.artemwhite.satellite.adapter.outbound.persistence.repositories.OrbitSnapshotJpaRepository
import com.artemwhite.satellite.adapter.outbound.persistence.repositories.SatelliteJpaRepository
import com.artemwhite.satellite.adapter.outbound.persistence.repositories.TleRecordJpaRepository
import com.artemwhite.satellite.domain.models.ActiveTle
import com.artemwhite.satellite.domain.models.TleRecord
import com.artemwhite.satellite.integration.SatelliteTestConfig.StubObjectRegistryClient
import com.artemwhite.satellite.integration.SatelliteTestConfig.StubTleProvider
import com.artemwhite.shared.model.ObjectRef
import com.artemwhite.shared.model.enums.ObjectCategory
import com.artemwhite.shared.model.enums.ObjectStatus
import java.time.Instant
import java.util.Locale
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import tools.jackson.databind.json.JsonMapper

@SpringBootTest
@ActiveProfiles("integration")
@Import(SatelliteTestConfig::class)
class SatelliteIntegrationTest {

    @Autowired
    lateinit var context: WebApplicationContext

    @Autowired
    lateinit var objectMapper: JsonMapper

    @Autowired
    lateinit var satelliteJpaRepository: SatelliteJpaRepository

    @Autowired
    lateinit var tleJpaRepository: TleRecordJpaRepository

    @Autowired
    lateinit var snapshotJpaRepository: OrbitSnapshotJpaRepository

    @Autowired
    lateinit var registry: StubObjectRegistryClient

    @Autowired
    lateinit var tleProvider: StubTleProvider

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
        snapshotJpaRepository.deleteAll()
        tleJpaRepository.deleteAll()
        satelliteJpaRepository.deleteAll()
        registry.objects.clear()
        registry.statusUpdates.clear()
        tleProvider.tles.clear()
    }

    @Test
    fun `register sync and position flow end to end`() {
        registry.objects[ISS_ID] = ObjectRef(ISS_ID, "ISS", ObjectCategory.SATELLITE)
        tleProvider.tles += activeTle()

        mockMvc.perform(
            post("/api/v1/admin/satellites")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"objectId":"$ISS_ID","noradId":25544}"""),
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.objectId").value(ISS_ID.toString()))
            .andExpect(jsonPath("$.noradId").value(25544))
            .andExpect(jsonPath("$.status").value("ACTIVE"))

        mockMvc.perform(post("/api/v1/admin/sync"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.updated").value(1))
            .andExpect(jsonPath("$.failed").value(0))
            .andExpect(jsonPath("$.decayed").value(0))

        mockMvc.perform(get("/api/v1/satellites/{id}/position", ISS_ID))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.x").isNumber)
            .andExpect(jsonPath("$.y").isNumber)
            .andExpect(jsonPath("$.z").isNumber)

        mockMvc.perform(get("/api/v1/satellites/{id}/timeline", ISS_ID).param("page", "0").param("size", "10"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.total").value(1))
            .andExpect(jsonPath("$.page").value(0))
            .andExpect(jsonPath("$.items.length()").value(1))
    }

    @Test
    fun `sync decays a tracked satellite missing from the TLE set`() {
        registry.objects[ISS_ID] = ObjectRef(ISS_ID, "ISS", ObjectCategory.SATELLITE)

        mockMvc.perform(
            post("/api/v1/admin/satellites")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"objectId":"$ISS_ID","noradId":25544}"""),
        )
            .andExpect(status().isCreated)

        mockMvc.perform(post("/api/v1/admin/sync"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.updated").value(0))
            .andExpect(jsonPath("$.decayed").value(1))

        assertThat(registry.statusUpdates).containsExactly(ISS_ID to ObjectStatus.DECAYED)

        mockMvc.perform(get("/api/v1/satellites/{id}/position", ISS_ID))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.type").value("satellite-not-found"))
    }

    @Test
    fun `position returns 404 for an object the catalog does not know`() {
        mockMvc.perform(get("/api/v1/satellites/{id}/position", UUID.randomUUID()))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.type").value("object-not-found"))
    }

    @Test
    fun `register rejects a non-positive norad id`() {
        mockMvc.perform(
            post("/api/v1/admin/satellites")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"objectId":"$ISS_ID","noradId":0}"""),
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.type").value("validation-error"))
    }

    private fun activeTle(): ActiveTle = ActiveTle(
        noradId = 25544,
        tle = TleRecord(
            line1 = line1(),
            line2 = line2(),
            epoch = Instant.parse("2024-04-09T00:00:00Z"),
            fetchedAt = Instant.parse("2026-01-01T00:00:00Z"),
        ),
    )

    private fun line1(): String {
        val chars = CharArray(69) { ' ' }
        chars[0] = '1'
        "25544".forEachIndexed { i, c -> chars[2 + i] = c }
        "24".forEachIndexed { i, c -> chars[18 + i] = c }
        String.format(Locale.US, "%012.8f", 100.0).forEachIndexed { i, c -> chars[20 + i] = c }
        chars[68] = '0'
        return String(chars)
    }

    private fun line2(): String {
        val chars = CharArray(69) { ' ' }
        chars[0] = '2'
        "25544".forEachIndexed { i, c -> chars[2 + i] = c }
        String.format(Locale.US, "%8.4f", 51.6416).forEachIndexed { i, c -> chars[8 + i] = c }
        String.format(Locale.US, "%8.4f", 35.1234).forEachIndexed { i, c -> chars[17 + i] = c }
        String.format(Locale.US, "%8.4f", 45.6789).forEachIndexed { i, c -> chars[34 + i] = c }
        String.format(Locale.US, "%8.4f", 314.3210).forEachIndexed { i, c -> chars[43 + i] = c }
        String.format(Locale.US, "%11.8f", 15.5).forEachIndexed { i, c -> chars[52 + i] = c }
        chars[68] = '0'
        return String(chars)
    }

    companion object {
        private val ISS_ID: UUID = UUID.fromString("00000000-0000-0000-0000-000000000001")
    }
}
