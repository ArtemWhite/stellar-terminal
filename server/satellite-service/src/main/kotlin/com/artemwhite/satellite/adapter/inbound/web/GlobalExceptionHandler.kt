package com.artemwhite.satellite.adapter.inbound.web

import com.artemwhite.satellite.domain.exceptions.InvalidTleException
import com.artemwhite.satellite.domain.exceptions.NotASatelliteException
import com.artemwhite.satellite.domain.exceptions.ObjectNotFoundException
import com.artemwhite.satellite.domain.exceptions.SatelliteNotFoundException
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
    fun handleObjectNotFound(e: ObjectNotFoundException): ResponseEntity<ProblemDetail> =
        problem(HttpStatus.NOT_FOUND, "object-not-found", e.message)

    @ExceptionHandler(SatelliteNotFoundException::class)
    fun handleSatelliteNotFound(e: SatelliteNotFoundException): ResponseEntity<ProblemDetail> =
        problem(HttpStatus.NOT_FOUND, "satellite-not-found", e.message)

    @ExceptionHandler(NotASatelliteException::class)
    fun handleNotASatellite(e: NotASatelliteException): ResponseEntity<ProblemDetail> =
        problem(HttpStatus.BAD_REQUEST, "not-a-satellite", e.message)

    @ExceptionHandler(InvalidTleException::class)
    fun handleInvalidTle(e: InvalidTleException): ResponseEntity<ProblemDetail> =
        problem(HttpStatus.UNPROCESSABLE_CONTENT, "invalid-tle", e.message)

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
