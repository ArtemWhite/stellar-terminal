package com.artemwhite.user.domain.ports.outbound

import com.artemwhite.user.domain.models.Notification
import com.artemwhite.user.domain.models.User

interface NotificationSender {
    fun send(user: User, notification: Notification)
}
