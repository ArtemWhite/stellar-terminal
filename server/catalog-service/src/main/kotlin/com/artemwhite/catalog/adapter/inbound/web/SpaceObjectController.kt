package com.artemwhite.catalog.adapter.inbound.web

import com.artemwhite.catalog.adapter.inbound.web.dto.CategorizeRequest
import com.artemwhite.catalog.adapter.inbound.web.dto.CreateObjectRequest
import com.artemwhite.catalog.adapter.inbound.web.dto.SpaceObjectResponse
import com.artemwhite.catalog.adapter.inbound.web.dto.UpdateStatusRequest
import com.artemwhite.catalog.adapter.inbound.web.mappers.toCommand
import com.artemwhite.catalog.adapter.inbound.web.mappers.toResponse
import com.artemwhite.catalog.application.usecase.CategorizeObjectUseCase
import com.artemwhite.catalog.application.usecase.CreateObjectUseCase
import com.artemwhite.catalog.application.usecase.DeleteObjectUseCase
import com.artemwhite.catalog.application.usecase.GetObjectUseCase
import com.artemwhite.catalog.application.usecase.SearchObjectsUseCase
import com.artemwhite.catalog.application.usecase.UpdateObjectStatusUseCase
import com.artemwhite.catalog.domain.models.PageResult
import com.artemwhite.catalog.domain.models.SearchFilter
import com.artemwhite.shared.model.enums.ObjectCategory
import com.artemwhite.shared.model.enums.ObjectStatus
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/objects")
class SpaceObjectController(
    private val searchObjects: SearchObjectsUseCase,
    private val getObject: GetObjectUseCase,
    private val createObject: CreateObjectUseCase,
    private val categorizeObject: CategorizeObjectUseCase,
    private val updateStatus: UpdateObjectStatusUseCase,
    private val deleteObject: DeleteObjectUseCase,
) {

    @GetMapping
    fun search(
        @RequestParam(required = false) category: ObjectCategory?,
        @RequestParam(required = false) status: ObjectStatus?,
        @RequestParam(required = false) query: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
    ): PageResult<SpaceObjectResponse> =
        searchObjects.execute(
            SearchFilter(
                category = category,
                status = status,
                query = query,
                page = page.coerceAtLeast(0),
                size = size.coerceIn(1, MAX_PAGE_SIZE),
            ),
        ).toResponse()

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): SpaceObjectResponse =
        getObject.execute(id).toResponse()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: CreateObjectRequest): SpaceObjectResponse =
        createObject.execute(request.toCommand()).toResponse()

    @PutMapping("/{id}/category")
    fun categorize(
        @PathVariable id: UUID,
        @Valid @RequestBody request: CategorizeRequest,
    ): SpaceObjectResponse =
        categorizeObject.execute(id, request.category, request.subcategory).toResponse()

    @PatchMapping("/{id}/status")
    fun updateStatus(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateStatusRequest,
    ): SpaceObjectResponse =
        updateStatus.execute(id, request.status).toResponse()

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: UUID) {
        deleteObject.execute(id)
    }

    companion object {
        private const val MAX_PAGE_SIZE = 100
    }
}
