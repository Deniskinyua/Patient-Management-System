package com.pms.pmsmodule.Controllers;

import com.pms.pmsmodule.DTO.PatientRequestDT0;
import com.pms.pmsmodule.DTO.PatientResponseDTO;
import com.pms.pmsmodule.DTO.Validators.CreatePatientValidationGroup;
import com.pms.pmsmodule.Services.PatientService;
import com.pms.pmsmodule.model.Patient;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.Builder;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        //! Remember to refine with http responses
        return ResponseEntity.ok().body(allPatients);
    }

    // Get Patient By Id
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> getPatientById( @PathVariable UUID id){
        PatientResponseDTO patient = patientService.getPatientById(id);
        return ResponseEntity.ok().body(patient);
    }

    /**
     * GET : createPatient - Create a new Patient
     * @param patientRequest
     * @return
     */

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(
            @Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDT0 patientRequest){
        PatientResponseDTO patientResponseDTO = patientService.createPatient(patientRequest);
        //! Remember to refine with http responses
        return ResponseEntity.ok().body(patientResponseDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatientData(
            @PathVariable UUID id, @Validated({Default.class}) @RequestBody PatientRequestDT0 patientData){

        PatientResponseDTO updatedPatientData = patientService.updatePatientData(id, patientData);
        return ResponseEntity.ok().body(updatedPatientData);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id){
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

}
