package com.pms.pmsmodule.Controllers;

import com.pms.pmsmodule.DTO.PatientRequestDT0;
import com.pms.pmsmodule.DTO.PatientResponseDTO;
import com.pms.pmsmodule.DTO.Validators.CreatePatientValidationGroup;
import com.pms.pmsmodule.Helpers.ApiPaths;
import com.pms.pmsmodule.Helpers.ApiResponse;
import com.pms.pmsmodule.Services.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * REST controller responsible for handling patient-related HTTP requests.
 * Provides endpoints for creating, retrieving, updating, and deleting patient records.
 *
 * Versioning and module-based grouping are handled via the ApiPaths and @Tag annotations.
 */
@RestController
@RequestMapping(ApiPaths.PATIENTS)
@Tag(name = "Patient", description = "Api for managing patients")
public class PatientController {

    private final PatientService patientService;

    /**
     * Constructor-based dependency injection of the PatientService.
     *
     * @param patientService service handling patient business logic
     */
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Retrieve all patients in the system.
     *
     * @return List of PatientResponseDTOs wrapped in a ResponseEntity
     */
    @GetMapping
    @Operation(summary = "Get all patients", description = "Retrieve a list of all patients in the system.")
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
        List<PatientResponseDTO> allPatients = patientService.getAllPatients();
        return ResponseEntity.status(HttpStatus.OK).body(allPatients);
    }

    /**
     * Retrieve a specific patient by their unique identifier.
     *
     * @param id UUID of the patient
     * @return PatientResponseDTO wrapped in a ResponseEntity
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a patient by ID", description = "Retrieve a patient's details using their unique ID.")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable UUID id) {
        PatientResponseDTO patient = patientService.getPatientById(id);
        return ResponseEntity.status(HttpStatus.OK).body(patient);
    }

    /**
     * Create a new patient entry in the system.
     *
     * Uses validation groups to differentiate between creation and update operations.
     *
     * @param patientRequest DTO containing new patient data
     * @return Success message with timestamp wrapped in a ResponseEntity
     */
    @PostMapping
    @Operation(summary = "Create a new patient", description = "Add a new patient to the system.")
    public ResponseEntity<ApiResponse> createPatient(
            @Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDT0 patientRequest) {

        patientService.createPatient(patientRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Patient successfully created", LocalDateTime.now()));
    }

    /**
     * Update an existing patient’s data using their unique ID.
     *
     * @param id UUID of the patient to update
     * @param patientData DTO containing updated patient data
     * @return Success message with timestamp wrapped in a ResponseEntity
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update patient data", description = "Update an existing patient's information using their ID.")
    public ResponseEntity<ApiResponse> updatePatientData(
            @PathVariable UUID id,
            @Validated({Default.class}) @RequestBody PatientRequestDT0 patientData) {

        patientService.updatePatientData(id, patientData);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse("Patient successfully updated", LocalDateTime.now()));
    }

    /**
     * Delete a patient record from the system using their unique ID.
     *
     * @param id UUID of the patient to delete
     * @return Success message with timestamp wrapped in a ResponseEntity
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a patient", description = "Remove a patient from the system using their ID.")
    public ResponseEntity<ApiResponse> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse("Patient successfully deleted", LocalDateTime.now()));
    }
}
