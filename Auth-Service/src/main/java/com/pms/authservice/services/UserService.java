package com.pms.authservice.services;

import com.pms.authservice.model.User;
import com.pms.authservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing user-related operations.
 * <p>
 * Provides methods to interact with the user repository and implement business logic
 * related to user management, such as retrieving users by email.
 * </p>
 *
 * <p><b>Usage:</b> Inject this service into other components or services that require user data access or business logic.</p>
 *
 * @author Denis Kinyua
 * @since 1.0
 */
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Finds a user by their email address.
     * <p>
     * This method can be extended to include additional business logic as needed.
     * </p>
     *
     * @param email the email address of the user to find
     * @return an {@link Optional} containing the {@link User} if found, or empty if not found
     */
    public Optional<User> findByEmail(String email){
        // Implement any business logic here

        return userRepository.findByEmail(email);
    }
}