package com.artemwhite.user.domain.exceptions

import java.util.UUID

class UserNotFoundException(val userId: UUID) : DomainException("User $userId not found")
