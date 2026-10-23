package com.artemwhite.user.integration

import com.artemwhite.shared.model.ObjectRef
import com.artemwhite.user.domain.exceptions.ObjectNotFoundException
import com.artemwhite.user.domain.models.Notification
import com.artemwhite.user.domain.models.User
import com.artemwhite.user.domain.ports.outbound.NotificationSender
import com.artemwhite.user.domain.ports.outbound.ObjectRegistryClient
import java.util.UUID
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

/**
 * Replaces the outbound HTTP ports with in-memory stubs for the integration
 * tests. The `@Primary` beans shadow the real RestClient adapter so the DB-backed
 * tests never reach the catalog-service.
 */
@TestConfiguration
class UserTestConfig {

    class StubObjectRegistryClient : ObjectRegistryClient {
        val objects = LinkedHashMap<UUID, ObjectRef>()

        override fun getObject(objectId: UUID): ObjectRef =
            objects[objectId] ?: throw ObjectNotFoundException(objectId)
    }

    class RecordingNotificationSender : NotificationSender {
        val sent = mutableListOf<Pair<User, Notification>>()

        override fun send(user: User, notification: Notification) {
            sent += user to notification
        }
    }

    @Bean
    @Primary
    fun objectRegistryClient(): StubObjectRegistryClient = StubObjectRegistryClient()

    @Bean
    @Primary
    fun notificationSender(): RecordingNotificationSender = RecordingNotificationSender()
}
