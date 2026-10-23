package com.artemwhite.user.adapter.inbound.web.mappers

import com.artemwhite.shared.model.enums.EventType
import com.artemwhite.shared.model.enums.ObjectStatus
import com.artemwhite.user.adapter.inbound.web.dto.RegisterUserRequest
import com.artemwhite.user.adapter.inbound.web.dto.SatelliteEventRequest
import com.artemwhite.user.application.usecase.AuthResult
import com.artemwhite.user.application.usecase.ResolvedFavorite
import com.artemwhite.user.domain.events.DecayedEvent
import com.artemwhite.user.domain.events.PassOverEvent
import com.artemwhite.user.domain.events.StatusChangedEvent
import com.artemwhite.user.domain.models.Collection
import com.artemwhite.user.domain.models.Favorite
import com.artemwhite.user.domain.models.PageResult
import com.artemwhite.user.testutil.UserFixtures.NOW
import com.artemwhite.user.testutil.UserFixtures.OBJECT_ID
import com.artemwhite.user.testutil.UserFixtures.collection
import com.artemwhite.user.testutil.UserFixtures.favorite
import com.artemwhite.user.testutil.UserFixtures.notification
import com.artemwhite.user.testutil.UserFixtures.objectRef
import com.artemwhite.user.testutil.UserFixtures.preset
import com.artemwhite.user.testutil.UserFixtures.user
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class UserWebMapperTest {

    @Test
    fun `maps a register request to a command`() {
        val command = RegisterUserRequest("tester", "test@example.com", "secret123").toCommand()

        assertThat(command.nickname).isEqualTo("tester")
        assertThat(command.email.value).isEqualTo("test@example.com")
        assertThat(command.password).isEqualTo("secret123")
    }

    @Test
    fun `maps a user to a response`() {
        val u = user()

        val response = u.toResponse()

        assertThat(response.id).isEqualTo(u.id)
        assertThat(response.nickname).isEqualTo(u.nickname)
        assertThat(response.email).isEqualTo(u.email.value)
        assertThat(response.role).isEqualTo(u.role)
        assertThat(response.createdAt).isEqualTo(u.createdAt)
    }

    @Test
    fun `maps an auth result to a response`() {
        val u = user()

        val response = AuthResult(user = u, token = "token-abc").toResponse()

        assertThat(response.user.id).isEqualTo(u.id)
        assertThat(response.token).isEqualTo("token-abc")
    }

    @Test
    fun `maps a notification and its page`() {
        val n = notification(userId = UUID.randomUUID())

        val single = n.toResponse()
        val page = PageResult(listOf(n), 0, 10, 1L).toResponse()

        assertThat(single.id).isEqualTo(n.id)
        assertThat(single.read).isEqualTo(n.read)
        assertThat(page.items).hasSize(1)
        assertThat(page.total).isEqualTo(1L)
    }

    @Test
    fun `maps a resolved favorite and its page`() {
        val favorite = favorite(UUID.randomUUID(), OBJECT_ID, NOW)
        val resolved = ResolvedFavorite(favorite, objectRef(name = "ISS"))

        val single = resolved.toResponse()
        val page = PageResult(listOf(resolved), 0, 10, 1L).toResponse()

        assertThat(single.objectId).isEqualTo(OBJECT_ID)
        assertThat(single.name).isEqualTo("ISS")
        assertThat(page.items).hasSize(1)
    }

    @Test
    fun `maps a preset and its page`() {
        val p = preset(userId = UUID.randomUUID())

        val single = p.toResponse()
        val page = PageResult(listOf(p), 0, 10, 1L).toResponse()

        assertThat(single.colors).isEqualTo(p.colors)
        assertThat(single.scale).isEqualTo(p.scale)
        assertThat(page.items).hasSize(1)
    }

    @Test
    fun `maps a collection and its page`() {
        val c = collection(userId = UUID.randomUUID(), objectIds = listOf(OBJECT_ID))

        val single = c.toResponse()
        val page = PageResult(listOf(c), 0, 10, 1L).toResponse()

        assertThat(single.objectIds).containsExactly(OBJECT_ID)
        assertThat(page.items).hasSize(1)
    }

    @Test
    fun `maps a status-changed event request`() {
        val event = SatelliteEventRequest(OBJECT_ID, ObjectStatus.DECAYED, EventType.STATUS_CHANGED, NOW).toDomainEvent()

        assertThat(event).isInstanceOf(StatusChangedEvent::class.java)
        assertThat((event as StatusChangedEvent).status).isEqualTo(ObjectStatus.DECAYED)
    }

    @Test
    fun `maps a decayed event request`() {
        val event = SatelliteEventRequest(OBJECT_ID, ObjectStatus.DECAYED, EventType.DECAYED, NOW).toDomainEvent()

        assertThat(event).isInstanceOf(DecayedEvent::class.java)
        assertThat(event.objectId).isEqualTo(OBJECT_ID)
    }

    @Test
    fun `maps a pass-over event request`() {
        val event = SatelliteEventRequest(OBJECT_ID, ObjectStatus.ACTIVE, EventType.PASS_OVER, NOW).toDomainEvent()

        assertThat(event).isInstanceOf(PassOverEvent::class.java)
    }
}
