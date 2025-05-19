package com.pms.pmsmodule.ExceptionHandlers;

import com.pms.pmsmodule.Repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the PMS module.
 * <p>
 * This class uses {@link ControllerAdvice} to intercept and handle exceptions across the application,
 * providing consistent and structured error responses to the client.
 * </p>
 * <p>
 * Each method handles a specific exception and maps it to a meaningful HTTP response.
 * </p>
 *
 * @author ---
 * @since 1.0
 */
@ControllerAdvice
@Component
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles validation errors thrown when a controller receives invalid method arguments.
     *
     * @param validationException the thrown {@link MethodArgumentNotValidException}.
     * @return a {@link ResponseEntity} containing a map of field names to error messages with HTTP 400 status.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException validationException) {

        Map<String, String> validationErrors = new HashMap<>();

        validationException.getBindingResult().getFieldErrors().forEach(
                error -> validationErrors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(validationErrors);
    }

    /**
     * Handles the case where a user tries to register or update a patient using an email that already exists.
     *
     * @param emailException the thrown {@link EmailAlreadyExistsException}.
     * @return a {@link ResponseEntity} with a user-friendly message and HTTP 400 status.
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException emailException) {

        log.warn("Email address already exists: {}", emailException.getMessage());

        Map<String, String> emailConflict = new HashMap<>();
        emailConflict.put("message", "Email already exists");

        return ResponseEntity.badRequest().body(emailConflict);
    }

    /**
     * Handles scenarios where the requested patient does not exist in the database.
     *
     * @param patientNotFound the thrown {@link PatientNotFoundException}.
     * @return a {@link ResponseEntity} with a "not found" message and HTTP 400 status.
     */
    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePatientNotFoundException(
            PatientNotFoundException patientNotFound) {

        log.warn("Patient not found: {}", patientNotFound.getMessage());

        Map<String, String> notFoundResponse = new HashMap<>();
        notFoundResponse.put("message", "Patient not found");

        return ResponseEntity.badRequest().body(notFoundResponse);
    }
}
