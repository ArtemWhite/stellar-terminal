package com.artemwhite.user.application.usecase

import com.artemwhite.user.domain.exceptions.NotificationNotFoundException
import com.artemwhite.user.testutil.FakeNotificationRepository
import com.artemwhite.user.testutil.UserFixtures.notification
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.UUID

class MarkNotificationReadUseCaseTest {

    private val repository = FakeNotificationRepository()
    private val useCase = MarkNotificationReadUseCase(repository)

    @Test
    fun `marks the notification as read`() {
        val n = notification(userId = UUID.randomUUID())
        repository.save(n)

        val result = useCase.execute(n.id)

        assertThat(result.read).isTrue()
        assertThat(repository.findById(n.id)!!.read).isTrue()
    }

    @Test
    fun `throws when the notification does not exist`() {
        assertThatThrownBy { useCase.execute(UUID.randomUUID()) }
            .isInstanceOf(NotificationNotFoundException::class.java)
    }
}
