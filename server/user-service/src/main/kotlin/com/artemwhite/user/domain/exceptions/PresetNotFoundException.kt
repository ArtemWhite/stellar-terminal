package com.artemwhite.user.domain.exceptions

import java.util.UUID

class PresetNotFoundException(val presetId: UUID) : DomainException("Preset $presetId not found")
