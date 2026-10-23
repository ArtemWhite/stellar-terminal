package com.artemwhite.catalog.domain.ports.outbound

import com.artemwhite.catalog.domain.models.SpaceObject
import com.artemwhite.catalog.domain.models.PageResult
import com.artemwhite.catalog.domain.models.SearchFilter
import java.util.UUID

interface SpaceObjectRepository {
    fun findById(id: UUID): SpaceObject?
    fun existsById(id: UUID): Boolean
    fun search(filter: SearchFilter): PageResult<SpaceObject>
    fun save(spaceObject: SpaceObject): SpaceObject
    fun deleteById(id: UUID)
}