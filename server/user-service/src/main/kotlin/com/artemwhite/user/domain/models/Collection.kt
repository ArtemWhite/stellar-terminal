package com.artemwhite.user.domain.models

import java.util.UUID

class Collection(
    val id: UUID,
    val userId: UUID,
    name: String,
    objectIds: List<UUID>,
) {
    var name: String = name
        private set

    private val _objectIds: MutableList<UUID> = objectIds.toMutableList()

    val objectIds: List<UUID>
        get() = _objectIds.toList()

    init {
        require(this.name.isNotBlank()) { "collection name must not be blank" }
    }

    fun rename(newName: String) {
        require(newName.isNotBlank()) { "collection name must not be blank" }
        name = newName
    }

    fun addObject(objectId: UUID) {
        if (objectId !in _objectIds) {
            _objectIds += objectId
        }
    }

    fun removeObject(objectId: UUID) {
        _objectIds.remove(objectId)
    }

    companion object {
        fun create(id: UUID, userId: UUID, name: String, objectIds: List<UUID> = emptyList()): Collection =
            Collection(id = id, userId = userId, name = name, objectIds = objectIds)
    }
}
