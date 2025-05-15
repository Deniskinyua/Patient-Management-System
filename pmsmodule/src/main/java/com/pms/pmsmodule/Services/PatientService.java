package com.pms.pmsmodule.Services;

import com.pms.pmsmodule.DTO.PatientRequestDT0;
import com.pms.pmsmodule.DTO.PatientResponseDTO;
import com.pms.pmsmodule.ExceptionHandlers.EmailAlreadyExistsException;
import com.pms.pmsmodule.ExceptionHandlers.PatientNotFoundException;
import com.pms.pmsmodule.Mapper.PatientMapper;
import com.pms.pmsmodule.Repository.PatientRepository;
import com.pms.pmsmodule.model.Patient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
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
        //.map(patient -> PatientMapper.mapModelToDTO(patient));

        return patients.stream()
                .map(PatientMapper::mapModelToDTO)
                .toList();
    }
    // Create a patient
    public PatientResponseDTO createPatient(PatientRequestDT0 patientRequestDT0){
        if(patientRepository.existsByEmail(patientRequestDT0.getEmail())){
            throw new EmailAlreadyExistsException("An patient with this email already exists"+patientRequestDT0);
        }
        Patient newPatient = patientRepository.save(PatientMapper.mapDTOtoModel(patientRequestDT0));

        return PatientMapper.mapModelToDTO(newPatient);
    }
    //Update patient data
    public PatientResponseDTO updatePatientData(UUID patientId, PatientRequestDT0 patientData) {

        Patient getPatient = patientRepository.findById(patientId).orElseThrow(
                () -> new PatientNotFoundException("Patient not found"));
        if (patientRepository.existsByEmailAndIdNot(patientData.getEmail(), patientId)) {
            throw new EmailAlreadyExistsException("A patient with this email already exists " + patientData);
        }
        getPatient.setName(patientData.getName());
        getPatient.setAddress(patientData.getAddress());
        getPatient.setEmail(patientData.getEmail());
        getPatient.setDateOfBirth(LocalDate.parse(patientData.getDateOfBirth()));
        Patient updatedPatient = patientRepository.save(getPatient);
        return PatientMapper.mapModelToDTO(updatedPatient);
    }
}
