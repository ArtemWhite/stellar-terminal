package com.artemwhite.catalog.adapter.inbound.web

import com.artemwhite.catalog.domain.exceptions.InvalidCategoryException
import com.artemwhite.catalog.domain.exceptions.ObjectNotFoundException
import com.artemwhite.shared.error.ProblemDetail
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException::class)
    fun handleNotFound(e: ObjectNotFoundException): ResponseEntity<ProblemDetail> =
        problem(HttpStatus.NOT_FOUND, "object-not-found", e.message)

    @ExceptionHandler(InvalidCategoryException::class)
    fun handleInvalidCategory(e: InvalidCategoryException): ResponseEntity<ProblemDetail> =
        problem(HttpStatus.BAD_REQUEST, "invalid-category", e.message)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(e: MethodArgumentNotValidException): ResponseEntity<ProblemDetail> =
        problem(
            HttpStatus.BAD_REQUEST,
            "validation-error",
            e.bindingResult.allErrors.joinToString { it.defaultMessage ?: "invalid input" },
        )

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(e: MethodArgumentTypeMismatchException): ResponseEntity<ProblemDetail> =
        problem(
            HttpStatus.BAD_REQUEST,
            "invalid-argument",
            "Invalid value for parameter '${e.name}'",
        )

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
