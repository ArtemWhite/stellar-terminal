package com.artemwhite.user.adapter.inbound.web

import com.artemwhite.user.adapter.inbound.web.dto.PresetRequest
import com.artemwhite.user.adapter.inbound.web.dto.PresetResponse
import com.artemwhite.user.adapter.inbound.web.mappers.toResponse
import com.artemwhite.user.application.command.SavePresetCommand
import com.artemwhite.user.application.usecase.ListPresetsUseCase
import com.artemwhite.user.application.usecase.SavePresetUseCase
import com.artemwhite.user.application.usecase.UpdatePresetUseCase
import com.artemwhite.user.domain.models.PageResult
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class PresetController(
    private val savePreset: SavePresetUseCase,
    private val updatePreset: UpdatePresetUseCase,
    private val listPresets: ListPresetsUseCase,
) {

    @GetMapping("/api/v1/users/{userId}/presets")
    fun list(
        @PathVariable userId: UUID,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
    ): PageResult<PresetResponse> =
        listPresets.execute(userId, page.coerceAtLeast(0), size.coerceIn(1, MAX_PAGE_SIZE)).toResponse()

    @PostMapping("/api/v1/users/{userId}/presets")
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@PathVariable userId: UUID, @Valid @RequestBody request: PresetRequest): PresetResponse =
        savePreset.execute(SavePresetCommand(userId, request.colors, request.scale)).toResponse()

    @PutMapping("/api/v1/presets/{id}")
    fun update(@PathVariable id: UUID, @Valid @RequestBody request: PresetRequest): PresetResponse =
        updatePreset.execute(id, request.colors, request.scale).toResponse()

    companion object {
        private const val MAX_PAGE_SIZE = 100
    }
}
