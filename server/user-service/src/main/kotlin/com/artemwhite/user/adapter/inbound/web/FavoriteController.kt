package com.artemwhite.user.adapter.inbound.web

import com.artemwhite.user.adapter.inbound.web.dto.AddFavoriteRequest
import com.artemwhite.user.adapter.inbound.web.dto.FavoriteResponse
import com.artemwhite.user.adapter.inbound.web.mappers.toResponse
import com.artemwhite.user.application.usecase.AddFavoriteUseCase
import com.artemwhite.user.application.usecase.ListFavoritesUseCase
import com.artemwhite.user.application.usecase.RemoveFavoriteUseCase
import com.artemwhite.user.domain.models.PageResult
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users/{userId}/favorites")
class FavoriteController(
    private val addFavorite: AddFavoriteUseCase,
    private val removeFavorite: RemoveFavoriteUseCase,
    private val listFavorites: ListFavoritesUseCase,
) {

    @GetMapping
    fun list(
        @PathVariable userId: UUID,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
    ): PageResult<FavoriteResponse> =
        listFavorites.execute(userId, page.coerceAtLeast(0), size.coerceIn(1, MAX_PAGE_SIZE)).toResponse()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun add(@PathVariable userId: UUID, @Valid @RequestBody request: AddFavoriteRequest): FavoriteResponse =
        addFavorite.execute(userId, request.objectId).toResponse()

    @DeleteMapping("/{objectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun remove(@PathVariable userId: UUID, @PathVariable objectId: UUID) {
        removeFavorite.execute(userId, objectId)
    }

    companion object {
        private const val MAX_PAGE_SIZE = 100
    }
}
