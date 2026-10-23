package com.artemwhite.user.domain.exceptions

import java.util.UUID

class FavoriteAlreadyExistsException(val userId: UUID, val objectId: UUID) :
    DomainException("Object $objectId is already in the favorites of user $userId")
