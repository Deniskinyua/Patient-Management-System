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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing patient-related operations.
 * Handles patient creation, update, retrieval, and soft deletion logic.
 * Integrates with {@link PatientRepository} and uses DTOs for data transfer.
 *
 * @author DenisKinyua
 */
@Slf4j
@Service
public class PatientService {

    private final PatientRepository patientRepository;

    /**
     * Constructor for dependency injection.
     *
     * @param patientRepository repository used to access patient data
     */
    public PatientService(PatientRepository patientRepository){
        this.patientRepository = patientRepository;
    }

    /**
     * Retrieves a list of all active (non-deleted) patients.
     *
     * @return a list of {@link PatientResponseDTO} objects
     */
    @Cacheable(value = "PRODUCT_CACHE")
    public List<PatientResponseDTO> getAllPatients(){
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .filter(patient -> !patient.isDeleted())
                .map(PatientMapper::mapModelToDTO)
                .toList();
    }

    /**
     * Retrieves a patient by their unique identifier.
     *
     * @param id UUID of the patient
     * @return corresponding {@link PatientResponseDTO}
     * @throws PatientNotFoundException if the patient is not found or marked as deleted
     */
    @Cacheable(value = "PRODUCT_CACHE", key = "#result.patientId()")
    public PatientResponseDTO getPatientById(UUID id){
        Patient patient = patientRepository.findByIdAndDeletedFalse(id).orElseThrow(
                () -> new PatientNotFoundException("Patient not found"));

        return PatientMapper.mapModelToDTO(patient);
    }

    /**
     * Creates a new patient or reactivates a previously soft-deleted one.
     * Prevents duplicate emails for active patients.
     *
     * @param patientRequestDT0 the incoming patient data
     * @return the created or reactivated {@link PatientResponseDTO}
     * @throws EmailAlreadyExistsException if an active patient with the same email exists
     */
    @CachePut(value = "PRODUCT_CACHE", key = "#result.patientId()")
    public PatientResponseDTO createPatient(PatientRequestDT0 patientRequestDT0){
        String email = patientRequestDT0.getEmail();

        if (patientRepository.existsByEmailAndDeletedFalse(email)) {
            throw new EmailAlreadyExistsException("A patient with this email already exists");
        }

        Optional<Patient> softDeletedPatientOpt = patientRepository.findByEmailAndDeletedTrue(email);

        if (softDeletedPatientOpt.isPresent()) {
            Patient softDeletedPatient = softDeletedPatientOpt.get();

            softDeletedPatient.setDeleted(false);
            softDeletedPatient.setName(patientRequestDT0.getName());
            softDeletedPatient.setAddress(patientRequestDT0.getAddress());
            softDeletedPatient.setDateOfBirth(LocalDate.parse(patientRequestDT0.getDateOfBirth()));
            softDeletedPatient.setRegisteredDate(LocalDate.parse(patientRequestDT0.getRegisteredDate()));

            Patient reactivatedPatient = patientRepository.save(softDeletedPatient);
            return PatientMapper.mapModelToDTO(reactivatedPatient);
        }

        Patient newPatient = PatientMapper.mapDTOtoModel(patientRequestDT0);
        Patient savedPatient = patientRepository.save(newPatient);
        return PatientMapper.mapModelToDTO(savedPatient);
    }

    /**
     * Updates an existing patient's data.
     * Ensures no duplicate emails across different patients.
     *
     * @param patientId   the UUID of the patient to update
     * @param patientData the new patient data
     * @return updated {@link PatientResponseDTO}
     * @throws PatientNotFoundException if the patient doesn't exist or is deleted
     * @throws EmailAlreadyExistsException if another patient already uses the provided email
     */
    @CachePut(value = "PRODUCT_CACHE", key = "#result.patientId()")
    public PatientResponseDTO updatePatientData(UUID patientId, PatientRequestDT0 patientData) {
        Patient getPatient = patientRepository.findByIdAndDeletedFalse(patientId).orElseThrow(
                () -> new PatientNotFoundException("Patient not found"));

        if (patientRepository.existsByEmailAndIdNot(patientData.getEmail(), patientId)) {
            throw new EmailAlreadyExistsException("A patient with this email already exists");
        }

        getPatient.setName(patientData.getName());
        getPatient.setAddress(patientData.getAddress());
        getPatient.setEmail(patientData.getEmail());
        getPatient.setDateOfBirth(LocalDate.parse(patientData.getDateOfBirth()));

        Patient updatedPatient = patientRepository.save(getPatient);
        return PatientMapper.mapModelToDTO(updatedPatient);
    }

    /**
     * Soft deletes a patient by setting the "deleted" flag to true.
     * Uses a transactional context to ensure data consistency.
     * Logs deletion for traceability and audit purposes.
     *
     * @param id UUID of the patient to delete
     * @throws PatientNotFoundException if patient not found or already deleted
     */
    @Transactional
    @CacheEvict(value = "PRODUCT_CACHE", key = "#result.id()")
    public void deletePatient(UUID id){
        Patient patient = patientRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found."));

        patient.setDeleted(true);
        patientRepository.save(patient);

        // auditLogger.logAction("DELETE", "Patient", id.toString() + " deleted");

        log.info("Patient with Id: {} deleted", id);
    }
}
