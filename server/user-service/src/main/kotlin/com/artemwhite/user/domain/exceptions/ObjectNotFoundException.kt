package com.artemwhite.user.domain.exceptions

import java.util.UUID

class ObjectNotFoundException(val objectId: UUID) : DomainException("Object $objectId not found in catalog")
