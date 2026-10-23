package com.artemwhite.catalog.integration

import com.artemwhite.catalog.adapter.outbound.persistence.repositories.SpaceObjectJpaRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import tools.jackson.databind.json.JsonMapper
import java.util.UUID

@SpringBootTest
@ActiveProfiles("integration")
class SpaceObjectIntegrationTest {

    @Autowired
    lateinit var context: WebApplicationContext

    @Autowired
    lateinit var objectMapper: JsonMapper

    @Autowired
    lateinit var jpaRepository: SpaceObjectJpaRepository

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
        jpaRepository.deleteAll()
    }

    @Test
    fun `creates and fetches an object end to end`() {
        val response = mockMvc.perform(
            post("/api/v1/objects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(satelliteJson()),
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value("ISS"))
            .andExpect(jsonPath("$.status").value("ACTIVE"))
            .andReturn().response.contentAsString

        val id = objectMapper.readTree(response).get("id").asText()

        mockMvc.perform(get("/api/v1/objects/{id}", id))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.category").value("SATELLITE"))
    }

    @Test
    fun `searches objects by category`() {
        createObject("ISS", "SATELLITE", "SCIENTIFIC")
        createObject("Betelgeuse", "STAR", null)

        mockMvc.perform(get("/api/v1/objects").param("category", "SATELLITE"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.total").value(1))
            .andExpect(jsonPath("$.items[0].name").value("ISS"))
    }

    @Test
    fun `searches objects by status`() {
        createObject("ISS", "SATELLITE", "SCIENTIFIC")
        val debrisId = createObject("Debris", "SATELLITE", null)

        mockMvc.perform(
            patch("/api/v1/objects/{id}/status", debrisId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"status":"DECAYED"}"""),
        )
            .andExpect(status().isOk)

        mockMvc.perform(get("/api/v1/objects").param("status", "DECAYED"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.total").value(1))
            .andExpect(jsonPath("$.items[0].name").value("Debris"))
    }

    @Test
    fun `searches objects by name query case-insensitively`() {
        createObject("ISS", "SATELLITE", "SCIENTIFIC")
        createObject("Betelgeuse", "STAR", null)

        mockMvc.perform(get("/api/v1/objects").param("query", "iss"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.total").value(1))
            .andExpect(jsonPath("$.items[0].name").value("ISS"))
    }

    @Test
    fun `combines category and status filters`() {
        createObject("ISS", "SATELLITE", "SCIENTIFIC")
        createObject("Betelgeuse", "STAR", null)

        mockMvc.perform(
            get("/api/v1/objects")
                .param("category", "SATELLITE")
                .param("status", "ACTIVE"),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.total").value(1))
            .andExpect(jsonPath("$.items[0].name").value("ISS"))
    }

    @Test
    fun `paginates search results`() {
        createObject("ISS", "SATELLITE", "SCIENTIFIC")
        createObject("Hubble", "SATELLITE", null)
        createObject("Betelgeuse", "STAR", null)

        mockMvc.perform(get("/api/v1/objects").param("size", "2").param("page", "0"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.total").value(3))
            .andExpect(jsonPath("$.size").value(2))
            .andExpect(jsonPath("$.page").value(0))
            .andExpect(jsonPath("$.items.length()").value(2))
    }

    @Test
    fun `returns 404 for an unknown object`() {
        mockMvc.perform(get("/api/v1/objects/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.type").value("object-not-found"))
    }

    @Test
    fun `rejects a subcategory on a non-satellite category`() {
        val id = createObject("ISS", "SATELLITE", "SCIENTIFIC")

        mockMvc.perform(
            put("/api/v1/objects/{id}/category", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"category":"PLANET","subcategory":"WEATHER"}"""),
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.type").value("invalid-category"))
    }

    @Test
    fun `updates status and then deletes the object`() {
        val id = createObject("ISS", "SATELLITE", "SCIENTIFIC")

        mockMvc.perform(
            patch("/api/v1/objects/{id}/status", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"status":"DECAYED"}"""),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("DECAYED"))

        mockMvc.perform(delete("/api/v1/objects/{id}", id))
            .andExpect(status().isNoContent)

        mockMvc.perform(get("/api/v1/objects/{id}", id))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `rejects a blank name with a validation error`() {
        mockMvc.perform(
            post("/api/v1/objects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "name": "",
                      "category": "SATELLITE",
                      "subcategory": "SCIENTIFIC",
                      "description": "some description",
                      "sourceId": "src-1",
                      "sourceType": "MANUAL"
                    }
                    """.trimIndent(),
                ),
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.type").value("validation-error"))
    }

    @Test
    fun `rejects a subcategory on a non-satellite at creation`() {
        mockMvc.perform(
            post("/api/v1/objects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "name": "Earth",
                      "category": "PLANET",
                      "subcategory": "WEATHER",
                      "description": "some description",
                      "sourceId": "src-1",
                      "sourceType": "MANUAL"
                    }
                    """.trimIndent(),
                ),
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.type").value("invalid-category"))
    }

    private fun createObject(name: String, category: String, subcategory: String?): String {
        val subcategoryField = subcategory?.let { "\"subcategory\": \"$it\"," } ?: ""
        val payload = """
            {
              "name": "$name",
              "category": "$category",
              $subcategoryField
              "description": "some description",
              "sourceId": "src-1",
              "sourceType": "MANUAL"
            }
        """.trimIndent()

        val response = mockMvc.perform(
            post("/api/v1/objects").contentType(MediaType.APPLICATION_JSON).content(payload),
        )
            .andExpect(status().isCreated)
            .andReturn().response.contentAsString

        return objectMapper.readTree(response).get("id").asText()
    }

    private fun satelliteJson(): String = """
        {
          "name": "ISS",
          "category": "SATELLITE",
          "subcategory": "SCIENTIFIC",
          "description": "International Space Station",
          "sourceId": "25544",
          "sourceType": "CELESTRAK"
        }
    """.trimIndent()
}
