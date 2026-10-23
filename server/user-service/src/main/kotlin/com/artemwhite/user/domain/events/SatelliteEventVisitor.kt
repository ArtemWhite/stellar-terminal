package com.artemwhite.user.domain.events

interface SatelliteEventVisitor<R> {
    fun visitStatusChanged(event: StatusChangedEvent): R

    fun visitDecayed(event: DecayedEvent): R

    fun visitPassOver(event: PassOverEvent): R
}
