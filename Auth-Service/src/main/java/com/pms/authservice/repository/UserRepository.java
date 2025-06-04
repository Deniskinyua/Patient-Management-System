package com.pms.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pms.authservice.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
