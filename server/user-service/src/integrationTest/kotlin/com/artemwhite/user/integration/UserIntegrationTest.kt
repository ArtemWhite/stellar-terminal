package com.artemwhite.user.integration

import com.artemwhite.shared.model.ObjectRef
import com.artemwhite.shared.model.enums.ObjectCategory
import com.artemwhite.user.adapter.outbound.persistence.repositories.CollectionJpaRepository
import com.artemwhite.user.adapter.outbound.persistence.repositories.FavoriteJpaRepository
import com.artemwhite.user.adapter.outbound.persistence.repositories.NotificationJpaRepository
import com.artemwhite.user.adapter.outbound.persistence.repositories.UserJpaRepository
import com.artemwhite.user.adapter.outbound.persistence.repositories.VisualizationPresetJpaRepository
import com.artemwhite.user.integration.UserTestConfig.RecordingNotificationSender
import com.artemwhite.user.integration.UserTestConfig.StubObjectRegistryClient
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

@SpringBootTest
@ActiveProfiles("integration")
@Import(UserTestConfig::class)
class UserIntegrationTest {

    @Autowired
    lateinit var context: WebApplicationContext

    @Autowired
    lateinit var objectMapper: JsonMapper

    @Autowired
    lateinit var userJpaRepository: UserJpaRepository

    @Autowired
    lateinit var favoriteJpaRepository: FavoriteJpaRepository

    @Autowired
    lateinit var notificationJpaRepository: NotificationJpaRepository

    @Autowired
    lateinit var collectionJpaRepository: CollectionJpaRepository

    @Autowired
    lateinit var presetJpaRepository: VisualizationPresetJpaRepository

    @Autowired
    lateinit var registry: StubObjectRegistryClient

    @Autowired
    lateinit var sender: RecordingNotificationSender

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
        favoriteJpaRepository.deleteAll()
        notificationJpaRepository.deleteAll()
        collectionJpaRepository.deleteAll()
        presetJpaRepository.deleteAll()
        userJpaRepository.deleteAll()
        registry.objects.clear()
        sender.sent.clear()
    }

    @Test
    fun `register login and fetch user end to end`() {
        val userId = registerUser("tester", "tester@example.com")

        mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email":"tester@example.com","password":"secret123"}"""),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.token").isNotEmpty)
            .andExpect(jsonPath("$.user.nickname").value("tester"))

        mockMvc.perform(get("/api/v1/users/{id}", userId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.nickname").value("tester"))
            .andExpect(jsonPath("$.role").value("REGISTERED"))
    }

    @Test
    fun `favorite add list and remove flow`() {
        val userId = registerUser("fan", "fan@example.com")
        registry.objects[ISS_ID] = ObjectRef(ISS_ID, "ISS", ObjectCategory.SATELLITE)

        mockMvc.perform(
            post("/api/v1/users/{userId}/favorites", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"objectId":"$ISS_ID"}"""),
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value("ISS"))
            .andExpect(jsonPath("$.category").value("SATELLITE"))

        mockMvc.perform(get("/api/v1/users/{userId}/favorites", userId).param("page", "0").param("size", "10"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.total").value(1))
            .andExpect(jsonPath("$.items.length()").value(1))

        mockMvc.perform(delete("/api/v1/users/{userId}/favorites/{objectId}", userId, ISS_ID))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `event posts notifications to fans and can be marked read`() {
        val userId = registerUser("watcher", "watcher@example.com")
        registry.objects[ISS_ID] = ObjectRef(ISS_ID, "ISS", ObjectCategory.SATELLITE)

        mockMvc.perform(
            post("/api/v1/users/{userId}/favorites", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"objectId":"$ISS_ID"}"""),
        )
            .andExpect(status().isCreated)

        mockMvc.perform(
            post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "objectId": "$ISS_ID",
                      "status": "DECAYED",
                      "type": "DECAYED",
                      "occurredAt": "2026-01-01T00:00:00Z"
                    }
                    """.trimIndent(),
                ),
        )
            .andExpect(status().isAccepted)

        assertThat(sender.sent).hasSize(1)

        val result = mockMvc.perform(get("/api/v1/users/{userId}/notifications", userId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.total").value(1))
            .andExpect(jsonPath("$.items[0].read").value(false))
            .andReturn()

        val notificationId = objectMapper.readTree(result.response.contentAsString)
            .get("items")[0].get("id").asText()

        mockMvc.perform(patch("/api/v1/notifications/{id}/read", UUID.fromString(notificationId)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.read").value(true))
    }

    @Test
    fun `collection full lifecycle`() {
        val userId = registerUser("collector", "collector@example.com")

        val created = mockMvc.perform(
            post("/api/v1/users/{userId}/collections", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name":"My favourites"}"""),
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value("My favourites"))
            .andReturn()

        val collectionId = objectMapper.readTree(created.response.contentAsString).get("id").asText()

        mockMvc.perform(
            post("/api/v1/collections/{id}/objects", UUID.fromString(collectionId))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"objectId":"$ISS_ID"}"""),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.objectIds.length()").value(1))

        mockMvc.perform(get("/api/v1/users/{userId}/collections", userId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.total").value(1))

        mockMvc.perform(
            delete("/api/v1/collections/{id}/objects/{objectId}", UUID.fromString(collectionId), ISS_ID),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.objectIds.length()").value(0))

        mockMvc.perform(delete("/api/v1/collections/{id}", UUID.fromString(collectionId)))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `preset save update and list flow`() {
        val userId = registerUser("viewer", "viewer@example.com")

        val saved = mockMvc.perform(
            post("/api/v1/users/{userId}/presets", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"colors":"#00ff00","scale":2.0}"""),
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.colors").value("#00ff00"))
            .andReturn()

        val presetId = objectMapper.readTree(saved.response.contentAsString).get("id").asText()

        mockMvc.perform(
            put("/api/v1/presets/{id}", UUID.fromString(presetId))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"colors":"#0000ff","scale":4.0}"""),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.colors").value("#0000ff"))
            .andExpect(jsonPath("$.scale").value(4.0))

        mockMvc.perform(get("/api/v1/users/{userId}/presets", userId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.total").value(1))
    }

    @Test
    fun `register validates input and rejects duplicates`() {
        registerUser("dup", "dup@example.com")

        mockMvc.perform(
            post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"nickname":"","email":"not-an-email","password":"123"}"""),
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.type").value("validation-error"))

        mockMvc.perform(
            post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"nickname":"dup","email":"dup@example.com","password":"secret123"}"""),
        )
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.type").value("duplicate-email"))
    }

    @Test
    fun `login rejects unknown credentials and favorite rejects unknown object`() {
        mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email":"ghost@example.com","password":"secret123"}"""),
        )
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.type").value("invalid-credentials"))

        val userId = registerUser("lonely", "lonely@example.com")
        mockMvc.perform(
            post("/api/v1/users/{userId}/favorites", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"objectId":"$ISS_ID"}"""),
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.type").value("object-not-found"))
    }

    private fun registerUser(nickname: String, email: String, password: String = "secret123"): UUID {
        val result = mockMvc.perform(
            post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"nickname":"$nickname","email":"$email","password":"$password"}"""),
        )
            .andExpect(status().isCreated)
            .andReturn()

        return UUID.fromString(objectMapper.readTree(result.response.contentAsString).get("id").asText())
    }

    companion object {
        private val ISS_ID: UUID = UUID.fromString("00000000-0000-0000-0000-000000000001")
    }
}
