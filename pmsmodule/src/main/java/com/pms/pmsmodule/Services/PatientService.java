package com.pms.pmsmodule.Services;

import com.pms.pmsmodule.DTO.PatientRequestDT0;
import com.pms.pmsmodule.DTO.PatientResponseDTO;
import com.pms.pmsmodule.ExceptionHandlers.EmailAlreadyExistsException;
import com.pms.pmsmodule.ExceptionHandlers.PatientNotFoundException;
import com.pms.pmsmodule.Mapper.PatientMapper;
import com.pms.pmsmodule.Repository.PatientRepository;
import com.pms.pmsmodule.model.Patient;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
//! Remember to test si @Noargs and @AllArgs constructor annotations

@Slf4j
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
                .filter(patient -> !patient.isDeleted())
                .map(PatientMapper::mapModelToDTO)
                .toList();
    }
    // Get Patient by Id

    public PatientResponseDTO getPatientById(UUID id){
        //If the patient does not exit, return message to user : PatientNotFoundException
        //
        Patient patient = patientRepository.findByIdAndDeletedFalse(id).orElseThrow(
                ()-> new PatientNotFoundException("Patient not found"));

        return PatientMapper.mapModelToDTO(patient);
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

        Patient getPatient = patientRepository.findByIdAndDeletedFalse(patientId).orElseThrow(
                () -> new PatientNotFoundException("Patient not found"));

        if (patientRepository.existsByEmailAndIdNot(patientData.getEmail(), patientId)) {
            throw new EmailAlreadyExistsException("A patient with this email already exists ");
        }
        getPatient.setName(patientData.getName());
        getPatient.setAddress(patientData.getAddress());
        getPatient.setEmail(patientData.getEmail());
        getPatient.setDateOfBirth(LocalDate.parse(patientData.getDateOfBirth()));
        Patient updatedPatient = patientRepository.save(getPatient);
        return PatientMapper.mapModelToDTO(updatedPatient);
    }
    /**
     * deletePatient
     * @param
     * @return
     */
    @Transactional
    public void deletePatient(UUID id){
        Patient patient = patientRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found."));
        //soft delete by updating the column to true(1)
        patient.setDeleted(true);
        patientRepository.save(patient); //!Check here --

        //!Remember to add audit logging
        //Add Audit logging for activity tracking
        //auditLogger.logAction("DELETE", "Patient", id.toString()" "deleted");
        log.info("Patient with Id: {} deleted", id);
    }
}
