package com.pms.pmsmodule.Controllers;

import com.pms.pmsmodule.DTO.PatientRequestDT0;
import com.pms.pmsmodule.DTO.PatientResponseDTO;
import com.pms.pmsmodule.DTO.Validators.CreatePatientValidationGroup;
import com.pms.pmsmodule.Helpers.ApiResponse;
import com.pms.pmsmodule.Services.PatientService;
import com.pms.pmsmodule.model.Patient;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients(){
        List<PatientResponseDTO> allPatients = patientService.getAllPatients();
        return ResponseEntity.status(HttpStatus.OK).body(allPatients);
    }

    // Get Patient By Id
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> getPatientById( @PathVariable UUID id){
        PatientResponseDTO patient = patientService.getPatientById(id);
        return ResponseEntity.status(HttpStatus.OK).body(patient);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createPatient(
            @Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDT0 patientRequest){
         patientService.createPatient(patientRequest);

        return  ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse("Patient successfully created", LocalDateTime.now()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updatePatientData(
            @PathVariable UUID id, @Validated({Default.class}) @RequestBody PatientRequestDT0 patientData){

        PatientResponseDTO updatedPatientData = patientService.updatePatientData(id, patientData);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse("Patient successfully updated", LocalDateTime.now()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePatient(@PathVariable UUID id){
        patientService.deletePatient(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse("Patient successfully deleted", LocalDateTime.now()));
    }

}
