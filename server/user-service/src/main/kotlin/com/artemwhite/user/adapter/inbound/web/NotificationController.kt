package com.artemwhite.user.adapter.inbound.web

import com.artemwhite.user.adapter.inbound.web.dto.NotificationResponse
import com.artemwhite.user.adapter.inbound.web.mappers.toResponse
import com.artemwhite.user.application.usecase.ListNotificationsUseCase
import com.artemwhite.user.application.usecase.MarkNotificationReadUseCase
import com.artemwhite.user.domain.models.PageResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class NotificationController(
    private val listNotifications: ListNotificationsUseCase,
    private val markNotificationRead: MarkNotificationReadUseCase,
) {

    @GetMapping("/api/v1/users/{userId}/notifications")
    fun list(
        @PathVariable userId: UUID,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
    ): PageResult<NotificationResponse> =
        listNotifications.execute(userId, page.coerceAtLeast(0), size.coerceIn(1, MAX_PAGE_SIZE)).toResponse()

    @PatchMapping("/api/v1/notifications/{id}/read")
    fun markRead(@PathVariable id: UUID): NotificationResponse =
        markNotificationRead.execute(id).toResponse()

    companion object {
        private const val MAX_PAGE_SIZE = 100
    }
}
