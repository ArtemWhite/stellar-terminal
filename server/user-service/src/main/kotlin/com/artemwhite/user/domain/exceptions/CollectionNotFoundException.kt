package com.artemwhite.user.domain.exceptions

import java.util.UUID

class CollectionNotFoundException(val collectionId: UUID) : DomainException("Collection $collectionId not found")
