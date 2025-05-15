package com.pms.pmsmodule.ExceptionHandlers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException validationException){
        Map<String, String> exceptions = new HashMap<>();
        validationException.getBindingResult().getFieldErrors().forEach(
                exception -> exceptions.put(exception.getField(), exception.getDefaultMessage()));
        return ResponseEntity.badRequest().body(exceptions);
    }
}
