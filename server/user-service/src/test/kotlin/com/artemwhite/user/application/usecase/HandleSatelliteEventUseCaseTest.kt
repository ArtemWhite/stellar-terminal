package com.artemwhite.user.application.usecase

import com.artemwhite.user.domain.events.DecayedEvent
import com.artemwhite.user.domain.events.NotificationTextBuilder
import com.artemwhite.user.domain.models.Favorite
import com.artemwhite.user.domain.ports.outbound.Clock
import com.artemwhite.user.testutil.FakeFavoriteRepository
import com.artemwhite.user.testutil.FakeNotificationRepository
import com.artemwhite.user.testutil.FakeNotificationSender
import com.artemwhite.user.testutil.FakeUserRepository
import com.artemwhite.user.testutil.UserFixtures.NOW
import com.artemwhite.user.testutil.UserFixtures.OBJECT_ID
import com.artemwhite.user.testutil.UserFixtures.user
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class HandleSatelliteEventUseCaseTest {

    private val favorites = FakeFavoriteRepository()
    private val users = FakeUserRepository()
    private val notifications = FakeNotificationRepository()
    private val sender = FakeNotificationSender()
    private val clock = Clock { NOW }
    private val textBuilder = NotificationTextBuilder()
    private val useCase = HandleSatelliteEventUseCase(favorites, users, notifications, sender, clock, textBuilder)

    @Test
    fun `sends a notification to every user who favorited the object`() {
        val u1 = user()
        val u2 = user()
        users.save(u1)
        users.save(u2)
        favorites.save(Favorite.create(u1.id, OBJECT_ID, NOW))
        favorites.save(Favorite.create(u2.id, OBJECT_ID, NOW))

        useCase.execute(DecayedEvent(OBJECT_ID, NOW))

        assertThat(sender.sent).hasSize(2)
        assertThat(notifications.store.values).hasSize(2)
        assertThat(notifications.store.values.map { it.text }).allMatch { it.contains("decayed") }
        assertThat(sender.sent.map { it.second.text }).allMatch { it.contains(OBJECT_ID.toString()) }
    }

    @Test
    fun `skips a favorite whose user no longer exists`() {
        favorites.save(Favorite.create(UUID.randomUUID(), OBJECT_ID, NOW))

        useCase.execute(DecayedEvent(OBJECT_ID, NOW))

        assertThat(sender.sent).isEmpty()
        assertThat(notifications.store).isEmpty()
    }

    @Test
    fun `does nothing when nobody favorited the object`() {
        useCase.execute(DecayedEvent(OBJECT_ID, NOW))

        assertThat(sender.sent).isEmpty()
        assertThat(notifications.store).isEmpty()
    }
}
