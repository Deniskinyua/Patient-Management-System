package com.pms.pmsmodule.Controllers;

import com.pms.pmsmodule.DTO.PatientRequestDT0;
import com.pms.pmsmodule.DTO.PatientResponseDTO;
import com.pms.pmsmodule.Services.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@Valid @RequestBody PatientRequestDT0 patientRequest){
        PatientResponseDTO patientResponseDTO = patientService.createPatient(patientRequest);
        //! Remember to refine with http responses
        return ResponseEntity.ok().body(patientResponseDTO);
    }
}
