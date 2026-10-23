package com.artemwhite.user.adapter.outbound.notification

import com.artemwhite.user.domain.models.Notification
import com.artemwhite.user.domain.models.User
import com.artemwhite.user.domain.ports.outbound.NotificationSender
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class LoggingNotificationSender : NotificationSender {
    private val log = LoggerFactory.getLogger(LoggingNotificationSender::class.java)

    override fun send(user: User, notification: Notification) {
        log.info("Sending notification {} to user {} ({}): {}", notification.id, user.id, user.email, notification.text)
    }
}
