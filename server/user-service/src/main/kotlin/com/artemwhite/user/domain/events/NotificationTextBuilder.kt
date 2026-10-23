package com.artemwhite.user.domain.events

class NotificationTextBuilder : SatelliteEventVisitor<String> {
    override fun visitStatusChanged(event: StatusChangedEvent): String =
        "Status of object ${event.objectId} changed to ${event.status}"

    override fun visitDecayed(event: DecayedEvent): String =
        "Object ${event.objectId} has decayed from orbit"

    override fun visitPassOver(event: PassOverEvent): String =
        "Object ${event.objectId} will pass over you at ${event.occurredAt}"
}
