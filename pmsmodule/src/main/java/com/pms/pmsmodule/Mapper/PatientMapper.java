package com.pms.pmsmodule.Mapper;

import com.pms.pmsmodule.DTO.PatientRequestDT0;
import com.pms.pmsmodule.DTO.PatientResponseDTO;
import com.pms.pmsmodule.model.Patient;

import java.time.LocalDate;

public class PatientMapper {
    //Patient to DTO
    //! Remember to check refactoring
    public static PatientResponseDTO mapModelToDTO(Patient patient){
        PatientResponseDTO patientDTO = new PatientResponseDTO();
        patientDTO.setPatientId(patient.getId().toString());
        patientDTO.setPatientName(patient.getName().toString());
        patientDTO.setPatientEmail(patient.getEmail().toString());
        patientDTO.setPatientAddress(patient.getAddress().toString());
        patientDTO.setPatientAddress(patient.getAddress().toString());

        return patientDTO;
    }
    //DTO to patient entity
    public static Patient mapDTOtoModel(PatientRequestDT0 patientRequestDT0){
        Patient patient = new Patient();
        patient.setName(patientRequestDT0.getName());
        //patient.setId(patientRequestDT0.get);
        patient.setAddress(patientRequestDT0.getAddress());
        patient.setEmail(patientRequestDT0.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDT0.getDateOfBirth()));
        patient.setRegisteredDate(LocalDate.parse(patientRequestDT0.getRegisteredDate()));

        return patient;
    }
}
