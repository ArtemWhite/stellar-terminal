package com.artemwhite.user.adapter.inbound.web

import com.artemwhite.user.adapter.inbound.web.dto.UserResponse
import com.artemwhite.user.adapter.inbound.web.mappers.toResponse
import com.artemwhite.user.application.usecase.GetUserUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val getUser: GetUserUseCase,
) {
    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): UserResponse = getUser.execute(id).toResponse()
}
