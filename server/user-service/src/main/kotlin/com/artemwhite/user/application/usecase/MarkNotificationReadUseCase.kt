package com.artemwhite.user.application.usecase

import com.artemwhite.user.domain.exceptions.NotificationNotFoundException
import com.artemwhite.user.domain.models.Notification
import com.artemwhite.user.domain.ports.outbound.NotificationRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MarkNotificationReadUseCase(
    private val notificationRepository: NotificationRepository,
) {
    fun execute(id: UUID): Notification {
        val notification = notificationRepository.findById(id)
            ?: throw NotificationNotFoundException(id)
        notification.markAsRead()
        return notificationRepository.save(notification)
    }
}
