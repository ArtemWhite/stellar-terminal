package com.artemwhite.user.adapter.inbound.web

import com.artemwhite.shared.error.ProblemDetail
import com.artemwhite.user.domain.exceptions.CollectionNotFoundException
import com.artemwhite.user.domain.exceptions.DuplicateEmailException
import com.artemwhite.user.domain.exceptions.FavoriteAlreadyExistsException
import com.artemwhite.user.domain.exceptions.InvalidCredentialsException
import com.artemwhite.user.domain.exceptions.NotificationNotFoundException
import com.artemwhite.user.domain.exceptions.ObjectNotFoundException
import com.artemwhite.user.domain.exceptions.PresetNotFoundException
import com.artemwhite.user.domain.exceptions.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound(e: UserNotFoundException): ResponseEntity<ProblemDetail> =
        problem(HttpStatus.NOT_FOUND, "user-not-found", e.message)

    @ExceptionHandler(ObjectNotFoundException::class)
    fun handleObjectNotFound(e: ObjectNotFoundException): ResponseEntity<ProblemDetail> =
        problem(HttpStatus.NOT_FOUND, "object-not-found", e.message)

    @ExceptionHandler(PresetNotFoundException::class)
    fun handlePresetNotFound(e: PresetNotFoundException): ResponseEntity<ProblemDetail> =
        problem(HttpStatus.NOT_FOUND, "preset-not-found", e.message)

    @ExceptionHandler(CollectionNotFoundException::class)
    fun handleCollectionNotFound(e: CollectionNotFoundException): ResponseEntity<ProblemDetail> =
        problem(HttpStatus.NOT_FOUND, "collection-not-found", e.message)

    @ExceptionHandler(NotificationNotFoundException::class)
    fun handleNotificationNotFound(e: NotificationNotFoundException): ResponseEntity<ProblemDetail> =
        problem(HttpStatus.NOT_FOUND, "notification-not-found", e.message)

    @ExceptionHandler(DuplicateEmailException::class)
    fun handleDuplicateEmail(e: DuplicateEmailException): ResponseEntity<ProblemDetail> =
        problem(HttpStatus.CONFLICT, "duplicate-email", e.message)

    @ExceptionHandler(FavoriteAlreadyExistsException::class)
    fun handleFavoriteAlreadyExists(e: FavoriteAlreadyExistsException): ResponseEntity<ProblemDetail> =
        problem(HttpStatus.CONFLICT, "favorite-already-exists", e.message)

    @ExceptionHandler(InvalidCredentialsException::class)
    fun handleInvalidCredentials(e: InvalidCredentialsException): ResponseEntity<ProblemDetail> =
        problem(HttpStatus.UNAUTHORIZED, "invalid-credentials", e.message)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(e: MethodArgumentNotValidException): ResponseEntity<ProblemDetail> =
        problem(
            HttpStatus.BAD_REQUEST,
            "validation-error",
            e.bindingResult.allErrors.joinToString { it.defaultMessage ?: "invalid input" },
        )

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(e: MethodArgumentTypeMismatchException): ResponseEntity<ProblemDetail> =
        problem(HttpStatus.BAD_REQUEST, "invalid-argument", "Invalid value for parameter '${e.name}'")

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(e: IllegalArgumentException): ResponseEntity<ProblemDetail> =
        problem(HttpStatus.BAD_REQUEST, "invalid-argument", e.message)

    private fun problem(
        status: HttpStatus,
        type: String,
        detail: String?,
    ): ResponseEntity<ProblemDetail> =
        ResponseEntity.status(status).body(
            ProblemDetail(
                type = type,
                title = status.reasonPhrase,
                status = status.value(),
                detail = detail ?: status.reasonPhrase,
            ),
        )
}
