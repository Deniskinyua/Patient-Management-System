package com.pms.authservice.ExceptionHandlers;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Component
public class AuthExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException validationException) {

        Map<String, String> validationErrors = new HashMap<>();

        validationException.getBindingResult().getFieldErrors().forEach(
                error -> validationErrors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(validationErrors);
    }
}
