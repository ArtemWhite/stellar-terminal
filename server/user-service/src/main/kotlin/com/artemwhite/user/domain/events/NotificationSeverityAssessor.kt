package com.artemwhite.user.domain.events

class NotificationSeverityAssessor : SatelliteEventVisitor<NotificationSeverity> {
    override fun visitStatusChanged(event: StatusChangedEvent): NotificationSeverity =
        NotificationSeverity.INFO

    override fun visitDecayed(event: DecayedEvent): NotificationSeverity =
        NotificationSeverity.CRITICAL

    override fun visitPassOver(event: PassOverEvent): NotificationSeverity =
        NotificationSeverity.WARNING
}
