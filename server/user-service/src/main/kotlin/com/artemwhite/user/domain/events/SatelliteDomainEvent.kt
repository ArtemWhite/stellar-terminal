package com.artemwhite.user.domain.events

import com.artemwhite.shared.model.enums.ObjectStatus
import java.time.Instant
import java.util.UUID

sealed class SatelliteDomainEvent {
    abstract val objectId: UUID

    abstract fun <R> accept(visitor: SatelliteEventVisitor<R>): R
}

class StatusChangedEvent(
    override val objectId: UUID,
    val status: ObjectStatus,
    val occurredAt: Instant,
) : SatelliteDomainEvent() {
    override fun <R> accept(visitor: SatelliteEventVisitor<R>): R = visitor.visitStatusChanged(this)
}

class DecayedEvent(
    override val objectId: UUID,
    val occurredAt: Instant,
) : SatelliteDomainEvent() {
    override fun <R> accept(visitor: SatelliteEventVisitor<R>): R = visitor.visitDecayed(this)
}

class PassOverEvent(
    override val objectId: UUID,
    val occurredAt: Instant,
) : SatelliteDomainEvent() {
    override fun <R> accept(visitor: SatelliteEventVisitor<R>): R = visitor.visitPassOver(this)
}
