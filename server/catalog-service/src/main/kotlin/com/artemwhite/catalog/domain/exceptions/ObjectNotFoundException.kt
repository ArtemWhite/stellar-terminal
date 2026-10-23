package com.artemwhite.catalog.domain.exceptions

import java.util.UUID

class ObjectNotFoundException
    (val objectId: UUID) : DomainException("Space object $objectId not found") {
}
