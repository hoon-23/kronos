package com.kronos.infra.web

import com.kronos.domain.schedule.InvalidScheduleException
import com.kronos.domain.schedule.NaturalLanguageParseException
import com.kronos.domain.schedule.ScheduleConflictException
import com.kronos.domain.schedule.ScheduleNotFoundException
import com.kronos.infra.web.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ScheduleNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(e: ScheduleNotFoundException) =
        ErrorResponse(404, "Not Found", e.message!!)

    @ExceptionHandler(ScheduleConflictException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleConflict(e: ScheduleConflictException) =
        ErrorResponse(409, "Conflict", e.message!!)

    @ExceptionHandler(InvalidScheduleException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleInvalidSchedule(e: InvalidScheduleException) =
        ErrorResponse(400, "Bad Request", e.message!!)

    @ExceptionHandler(NaturalLanguageParseException::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun handleParseFailure(e: NaturalLanguageParseException) =
        ErrorResponse(422, "Unprocessable Entity", e.message!!)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidation(e: MethodArgumentNotValidException): ErrorResponse {
        val message = e.bindingResult.fieldErrors.joinToString(", ") {
            "${it.field}: ${it.defaultMessage}"
        }
        return ErrorResponse(400, "Bad Request", message)
    }
}
