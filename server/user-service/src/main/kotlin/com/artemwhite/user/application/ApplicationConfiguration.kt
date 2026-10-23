package com.artemwhite.user.application

import com.artemwhite.user.domain.events.NotificationTextBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfiguration {

    @Bean
    fun notificationTextBuilder(): NotificationTextBuilder = NotificationTextBuilder()
}
