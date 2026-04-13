package com.ceiba.fashtoll.exceptionHandling;

import com.ceiba.fashtoll.exceptionHandling.exceptionTypes.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException exception) {
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException exception) {
        ApiError apiError = new ApiError(
                HttpStatus.FORBIDDEN.value(),
                "No tiene permisos para realizar esta accion",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }
}
