package com.pms.pmsmodule.Repository;

import com.pms.pmsmodule.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for accessing and managing {@link Patient} entities.
 * <p>
 * Extends {@link JpaRepository} to provide standard CRUD operations,
 * as well as custom queries for handling soft deletion and email uniqueness.
 * </p>
 *
 * <p>
 * This interface is used by the {@link com.pms.pmsmodule.Services.PatientService}
 * to perform database interactions related to patient entities.
 * </p>
 *
 * @author ---
 * @since 1.0
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    /**
     * Checks whether a patient with the given email exists (including soft-deleted ones).
     *
     * @param email the email to check.
     * @return {@code true} if a patient exists with the provided email, {@code false} otherwise.
     */
    boolean existsByEmail(String email);

    /**
     * Checks if an active patient with the given email exists, excluding a specific patient ID.
     * Used to enforce email uniqueness during update operations.
     *
     * @param email the email to check.
     * @param id    the ID of the patient to exclude from the check.
     * @return {@code true} if another patient exists with the same email, {@code false} otherwise.
     */
    boolean existsByEmailAndIdNot(String email, UUID id);

    /**
     * Retrieves a patient by ID only if they are not marked as deleted (i.e., soft delete flag is false).
     *
     * @param id the UUID of the patient.
     * @return an {@link Optional} containing the patient if found and not deleted, empty otherwise.
     */
    Optional<Patient> findByIdAndDeletedFalse(UUID id);

    /**
     * Retrieves a soft-deleted patient by their email address.
     * Useful for restoration or reactivation logic.
     *
     * @param email the email of the patient.
     * @return an {@link Optional} containing the patient if found and deleted, empty otherwise.
     */
    Optional<Patient> findByEmailAndDeletedTrue(String email);

    /**
     * Checks whether a non-deleted (active) patient exists with the given email.
     *
     * @param email the email to check.
     * @return {@code true} if an active patient exists with the provided email, {@code false} otherwise.
     */
    boolean existsByEmailAndDeletedFalse(String email);
}
