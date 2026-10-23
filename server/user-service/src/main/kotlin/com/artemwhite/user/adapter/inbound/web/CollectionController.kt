package com.artemwhite.user.adapter.inbound.web

import com.artemwhite.user.adapter.inbound.web.dto.CollectionObjectRequest
import com.artemwhite.user.adapter.inbound.web.dto.CollectionRequest
import com.artemwhite.user.adapter.inbound.web.dto.CollectionResponse
import com.artemwhite.user.adapter.inbound.web.mappers.toResponse
import com.artemwhite.user.application.command.CreateCollectionCommand
import com.artemwhite.user.application.usecase.AddObjectToCollectionUseCase
import com.artemwhite.user.application.usecase.CreateCollectionUseCase
import com.artemwhite.user.application.usecase.DeleteCollectionUseCase
import com.artemwhite.user.application.usecase.ListCollectionsUseCase
import com.artemwhite.user.application.usecase.RemoveObjectFromCollectionUseCase
import com.artemwhite.user.domain.models.PageResult
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class CollectionController(
    private val createCollection: CreateCollectionUseCase,
    private val listCollections: ListCollectionsUseCase,
    private val deleteCollection: DeleteCollectionUseCase,
    private val addObject: AddObjectToCollectionUseCase,
    private val removeObject: RemoveObjectFromCollectionUseCase,
) {

    @GetMapping("/api/v1/users/{userId}/collections")
    fun list(
        @PathVariable userId: UUID,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
    ): PageResult<CollectionResponse> =
        listCollections.execute(userId, page.coerceAtLeast(0), size.coerceIn(1, MAX_PAGE_SIZE)).toResponse()

    @PostMapping("/api/v1/users/{userId}/collections")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@PathVariable userId: UUID, @Valid @RequestBody request: CollectionRequest): CollectionResponse =
        createCollection.execute(CreateCollectionCommand(userId, request.name)).toResponse()

    @DeleteMapping("/api/v1/collections/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: UUID) {
        deleteCollection.execute(id)
    }

    @PostMapping("/api/v1/collections/{id}/objects")
    fun addObject(@PathVariable id: UUID, @Valid @RequestBody request: CollectionObjectRequest): CollectionResponse =
        addObject.execute(id, request.objectId).toResponse()

    @DeleteMapping("/api/v1/collections/{id}/objects/{objectId}")
    fun removeObject(@PathVariable id: UUID, @PathVariable objectId: UUID): CollectionResponse =
        removeObject.execute(id, objectId).toResponse()

    companion object {
        private const val MAX_PAGE_SIZE = 100
    }
}
