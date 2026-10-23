package com.artemwhite.user.application.usecase

import com.artemwhite.user.testutil.FakeNotificationRepository
import com.artemwhite.user.testutil.UserFixtures.NOW
import com.artemwhite.user.testutil.UserFixtures.notification
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class ListNotificationsUseCaseTest {

    private val repository = FakeNotificationRepository()
    private val useCase = ListNotificationsUseCase(repository)

    @Test
    fun `returns the notifications of a user as a page`() {
        val userId = UUID.randomUUID()
        repository.save(notification(userId = userId))
        repository.save(notification(userId = UUID.randomUUID()))

        val result = useCase.execute(userId, 0, 20)

        assertThat(result.items).hasSize(1)
        assertThat(result.total).isEqualTo(1)
        assertThat(result.page).isEqualTo(0)
    }

    @Test
    fun `paginates notifications`() {
        val userId = UUID.randomUUID()
        repeat(3) { repository.save(notification(userId = userId, createdAt = NOW.plusSeconds(it.toLong()))) }

        val first = useCase.execute(userId, 0, 2)
        val second = useCase.execute(userId, 1, 2)

        assertThat(first.items).hasSize(2)
        assertThat(second.items).hasSize(1)
        assertThat(first.total).isEqualTo(3)
    }
}
