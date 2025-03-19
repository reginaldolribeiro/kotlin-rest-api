package com.kotlin.kotlinrestapi.controller

import com.kotlin.kotlinrestapi.exception.CustomerNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.time.OffsetDateTime

@ControllerAdvice
class GlobalExceptionHandler {

    @ControllerAdvice
    class GlobalExceptionHandler {
        @ExceptionHandler(CustomerNotFoundException::class)
        fun handleCustomerNotFoundException(
            ex: CustomerNotFoundException,
            request: WebRequest
        ): ResponseEntity<Map<String, Any?>> {
            val body = java.util.Map.of<String, Any?>(
                "timestamp", OffsetDateTime.now(),
                "status", HttpStatus.NOT_FOUND.value(),
                "error", "Not Found",
                "message", ex.message,
                "path", request.getDescription(false).replace("uri=", "")
            )
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body)
        }
    }

}