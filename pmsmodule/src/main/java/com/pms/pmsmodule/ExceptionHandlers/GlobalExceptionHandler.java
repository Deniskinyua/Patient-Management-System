package com.pms.pmsmodule.ExceptionHandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException validationException){
        Map<String, String> exceptions = new HashMap<>();
        validationException.getBindingResult().getFieldErrors().forEach(
                exception -> exceptions.put(exception.getField(), exception.getDefaultMessage()));
        return ResponseEntity.badRequest().body(exceptions);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException emailException){
        log.warn("Email address already exists{}", emailException.getMessage());
        Map<String, String> emailExceptions = new HashMap<>();
        emailExceptions.put("message", "Email already exists");
        return ResponseEntity.badRequest().body(emailExceptions);
    }
    //PatientNotFoundException
    public ResponseEntity<Map<String, String>> handlePatientNotFoundException(
            PatientNotFoundException patientNotFound){
        log.warn("Patient not found{}", patientNotFound.getMessage());
        Map<String, String> patientNotFoundException = new HashMap<>();
        patientNotFoundException.put("message", "Patient not found");
        return ResponseEntity.badRequest().body(patientNotFoundException);
    }
}
