package com.pms.authservice.services;

import com.pms.authservice.dto.LoginRequestDTO;
import com.pms.authservice.utils.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service responsible for authenticating users and generating JWT tokens.
 * <p>
 * This service validates user credentials and, upon successful authentication,
 * generates a JWT token containing user information.
 * </p>
 *
 * <p><b>Usage:</b> Inject this service into controllers or other components that require authentication logic.</p>
 *
 * @author Denis Kinyua
 * @since 1.0
 */
@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Authenticates a user based on the provided login credentials.
     * <p>
     * If the user exists and the password matches, a JWT token is generated and returned.
     * Otherwise, an empty {@link Optional} is returned.
     * </p>
     *
     * @param loginRequestDTO The login request containing the user's email and password.
     * @return An {@link Optional} containing the JWT token if authentication is successful, or empty if not.
     */
    public Optional<String> authenticateUser(LoginRequestDTO loginRequestDTO) {
        Optional<String> token = userService
                .findByEmail(loginRequestDTO.getEmail())
                .filter(p -> passwordEncoder.matches(loginRequestDTO.getPassword(), p.getPassword()))
                .map(u -> jwtUtil.generateToken(u.getEmail(), u.getRole()));

        return token;
    }

    /**
     * Validates the JWT token sent by the request
     * <p>
     *     If JWT token is successfully validated, return true else return false
     * </p>
     * @param jwtToken The token extracted from the request header
     * @return A boolean affirming validation or not
     */

    public boolean validateToken(String jwtToken) {
        try{
            jwtUtil.validateToken(jwtToken);
            return true;
        }
        catch (JwtException jwtException){
            return false;
        }
    }
}