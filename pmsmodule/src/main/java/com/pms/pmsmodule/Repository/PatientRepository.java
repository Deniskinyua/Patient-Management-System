package com.pms.pmsmodule.Repository;

import com.pms.pmsmodule.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, UUID id);
    /**
     * Create an optional since patient could be deleted
     */
    Optional<Patient> findByIdAndDeletedFalse(UUID id);
}
