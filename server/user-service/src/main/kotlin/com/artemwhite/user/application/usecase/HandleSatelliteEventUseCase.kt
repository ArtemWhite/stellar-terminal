package com.artemwhite.user.application.usecase

import com.artemwhite.user.domain.events.NotificationTextBuilder
import com.artemwhite.user.domain.events.SatelliteDomainEvent
import com.artemwhite.user.domain.models.Notification
import com.artemwhite.user.domain.ports.outbound.Clock
import com.artemwhite.user.domain.ports.outbound.FavoriteRepository
import com.artemwhite.user.domain.ports.outbound.NotificationRepository
import com.artemwhite.user.domain.ports.outbound.NotificationSender
import com.artemwhite.user.domain.ports.outbound.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class HandleSatelliteEventUseCase(
    private val favoriteRepository: FavoriteRepository,
    private val userRepository: UserRepository,
    private val notificationRepository: NotificationRepository,
    private val notificationSender: NotificationSender,
    private val clock: Clock,
    private val textBuilder: NotificationTextBuilder,
) {
    private val log = LoggerFactory.getLogger(HandleSatelliteEventUseCase::class.java)

    fun execute(event: SatelliteDomainEvent) {
        val userIds = favoriteRepository.findUserIdsByObject(event.objectId)
        for (userId in userIds) {
            val user = userRepository.findById(userId) ?: continue
            val notification = notificationRepository.save(
                Notification.create(
                    id = UUID.randomUUID(),
                    userId = userId,
                    text = event.accept(textBuilder),
                    now = clock.now(),
                ),
            )
            notificationSender.send(user, notification)
        }
        log.info("Handled {} for object {}: {} notifications", event::class.simpleName, event.objectId, userIds.size)
    }
}
