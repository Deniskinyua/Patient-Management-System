package com.pms.pmsmodule.Services;

import com.pms.pmsmodule.DTO.PatientRequestDT0;
import com.pms.pmsmodule.DTO.PatientResponseDTO;
import com.pms.pmsmodule.Mapper.PatientMapper;
import com.pms.pmsmodule.Repository.PatientRepository;
import com.pms.pmsmodule.model.Patient;
import org.springframework.stereotype.Service;

import java.util.List;
//! Remember to test si @Noargs and @AllArgs constructor annotations

@Service
public class PatientService {
    private final  PatientRepository patientRepository;

    //constructor
    public PatientService(PatientRepository patientRepository){
        this.patientRepository = patientRepository;
    }

    //Get all patients
    public List<PatientResponseDTO> getAllPatients(){
        List<Patient> patients = patientRepository.findAll();
        //.map(patient -> PatientMapper.mapModelToDTO(patient))

        return patients.stream()
                .map(PatientMapper::mapModelToDTO)
                .toList();
    }
    // Create a patient
    public PatientResponseDTO createPatient(PatientRequestDT0 patientRequestDT0){
        Patient newPatient = patientRepository.save(PatientMapper.mapDTOtoModel(patientRequestDT0));

        return PatientMapper.mapModelToDTO(newPatient);
    }
}
