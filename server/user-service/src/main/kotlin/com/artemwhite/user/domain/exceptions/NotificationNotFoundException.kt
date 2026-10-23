package com.artemwhite.user.domain.exceptions

import java.util.UUID

class NotificationNotFoundException(val notificationId: UUID) : DomainException("Notification $notificationId not found")
