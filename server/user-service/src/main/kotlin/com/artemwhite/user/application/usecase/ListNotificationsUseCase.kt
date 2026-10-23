package com.artemwhite.user.application.usecase

import com.artemwhite.user.domain.models.Notification
import com.artemwhite.user.domain.models.PageResult
import com.artemwhite.user.domain.ports.outbound.NotificationRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ListNotificationsUseCase(
    private val notificationRepository: NotificationRepository,
) {
    fun execute(userId: UUID, page: Int, size: Int): PageResult<Notification> =
        notificationRepository.findByUser(userId, page, size)
}
