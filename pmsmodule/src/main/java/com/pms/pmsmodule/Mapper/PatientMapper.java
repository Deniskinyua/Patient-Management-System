package com.pms.pmsmodule.Mapper;

import com.pms.pmsmodule.DTO.PatientRequestDT0;
import com.pms.pmsmodule.DTO.PatientResponseDTO;
import com.pms.pmsmodule.model.Patient;

import java.time.LocalDate;

/**
 * Utility class responsible for mapping between {@link Patient} entities and their corresponding DTOs.
 * <p>
 * This helps in separating concerns between persistence models and data exposed via APIs.
 * Mapping logic is encapsulated here to keep service layers clean and maintainable.
 * </p>
 *
 * <p>
 * This class follows a static utility pattern to avoid unnecessary instantiation.
 * </p>
 *
 * @author DenisKinyua
 * @since 1.0
 */
public class PatientMapper {

    /**
     * Converts a {@link Patient} entity to a {@link PatientResponseDTO}.
     *
     * @param patient the patient entity to convert.
     * @return a DTO containing patient details, or {@code null} if input is {@code null}.
     */
    public static PatientResponseDTO mapModelToDTO(Patient patient) {
        if (patient == null) return null;

        PatientResponseDTO patientDTO = new PatientResponseDTO();
        patientDTO.setPatientId(patient.getId().toString());
        patientDTO.setPatientName(patient.getName());
        patientDTO.setPatientEmail(patient.getEmail());
        patientDTO.setPatientAddress(patient.getAddress());
        patientDTO.setDateOfBirth(patient.getDateOfBirth().toString());

        return patientDTO;
    }

    /**
     * Converts a {@link PatientRequestDT0} to a {@link Patient} entity.
     * <p>
     * This is typically used when creating or updating patients from API requests.
     * </p>
     *
     * @param patientRequestDT0 the request DTO containing patient input data.
     * @return a {@link Patient} entity populated with input values.
     */
    public static Patient mapDTOtoModel(PatientRequestDT0 patientRequestDT0) {
        if (patientRequestDT0 == null) return null;

        Patient patient = new Patient();
        patient.setName(patientRequestDT0.getName());
        patient.setAddress(patientRequestDT0.getAddress());
        patient.setEmail(patientRequestDT0.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDT0.getDateOfBirth()));
        patient.setRegisteredDate(LocalDate.parse(patientRequestDT0.getRegisteredDate()));
        patient.setDeleted(false); // New patients are not deleted by default

        return patient;
    }
}
