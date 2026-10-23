package com.artemwhite.user.application.command

import java.util.UUID

data class SavePresetCommand(
    val userId: UUID,
    val colors: String,
    val scale: Double,
)
