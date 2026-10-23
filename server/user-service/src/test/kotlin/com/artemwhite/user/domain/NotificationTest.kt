package com.artemwhite.user.domain

import com.artemwhite.user.domain.models.Notification
import com.artemwhite.user.testutil.UserFixtures.NOW
import com.artemwhite.user.testutil.UserFixtures.notification
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.UUID

class NotificationTest {

    @Test
    fun `starts unread and can be marked read`() {
        val n = notification(userId = UUID.randomUUID())

        assertThat(n.read).isFalse()
        n.markAsRead()
        assertThat(n.read).isTrue()
    }

    @Test
    fun `rejects a blank text`() {
        assertThatThrownBy { Notification.create(UUID.randomUUID(), UUID.randomUUID(), "  ", NOW) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }
}
