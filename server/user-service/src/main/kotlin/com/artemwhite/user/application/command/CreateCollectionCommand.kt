package com.artemwhite.user.application.command

import java.util.UUID

data class CreateCollectionCommand(
    val userId: UUID,
    val name: String,
)
