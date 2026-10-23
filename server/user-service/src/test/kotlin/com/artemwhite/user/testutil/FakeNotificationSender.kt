package com.artemwhite.user.testutil

import com.artemwhite.user.domain.models.Notification
import com.artemwhite.user.domain.models.User
import com.artemwhite.user.domain.ports.outbound.NotificationSender

class FakeNotificationSender : NotificationSender {

    val sent = mutableListOf<Pair<User, Notification>>()

    override fun send(user: User, notification: Notification) {
        sent += user to notification
    }
}
