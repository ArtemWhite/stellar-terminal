package com.artemwhite.satellite.application.command

import java.util.UUID

data class RegisterSatelliteCommand(
    val objectId: UUID,
    val noradId: Int,
)
