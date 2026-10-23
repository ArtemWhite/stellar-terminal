package com.artemwhite.user.adapter.inbound.web

import com.artemwhite.user.domain.exceptions.CollectionNotFoundException
import com.artemwhite.user.domain.exceptions.DuplicateEmailException
import com.artemwhite.user.domain.exceptions.FavoriteAlreadyExistsException
import com.artemwhite.user.domain.exceptions.InvalidCredentialsException
import com.artemwhite.user.domain.exceptions.NotificationNotFoundException
import com.artemwhite.user.domain.exceptions.ObjectNotFoundException
import com.artemwhite.user.domain.exceptions.PresetNotFoundException
import com.artemwhite.user.domain.exceptions.UserNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.util.UUID

class GlobalExceptionHandlerTest {

    private val handler = GlobalExceptionHandler()

    @Test
    fun `maps not-found exceptions to 404`() {
        assertThat(handler.handleUserNotFound(UserNotFoundException(UUID.randomUUID())).statusCode)
            .isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(handler.handleObjectNotFound(ObjectNotFoundException(UUID.randomUUID())).statusCode)
            .isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(handler.handlePresetNotFound(PresetNotFoundException(UUID.randomUUID())).statusCode)
            .isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(handler.handleCollectionNotFound(CollectionNotFoundException(UUID.randomUUID())).statusCode)
            .isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(handler.handleNotificationNotFound(NotificationNotFoundException(UUID.randomUUID())).statusCode)
            .isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `maps conflict exceptions to 409`() {
        assertThat(handler.handleDuplicateEmail(DuplicateEmailException("x@example.com")).statusCode)
            .isEqualTo(HttpStatus.CONFLICT)
        assertThat(
            handler.handleFavoriteAlreadyExists(
                FavoriteAlreadyExistsException(UUID.randomUUID(), UUID.randomUUID()),
            ).statusCode,
        ).isEqualTo(HttpStatus.CONFLICT)
    }

    @Test
    fun `maps invalid credentials to 401`() {
        assertThat(handler.handleInvalidCredentials(InvalidCredentialsException()).statusCode)
            .isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun `maps an illegal argument to 400`() {
        assertThat(handler.handleIllegalArgument(IllegalArgumentException("bad")).statusCode)
            .isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `produces a problem detail with a type and status`() {
        val body = handler.handleUserNotFound(UserNotFoundException(UUID.randomUUID())).body!!

        assertThat(body.type).isEqualTo("user-not-found")
        assertThat(body.status).isEqualTo(404)
        assertThat(body.detail).contains("not found")
    }
}
