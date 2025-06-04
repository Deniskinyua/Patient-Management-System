package com.pms.authservice.controller;

import com.pms.authservice.dto.LoginRequestDTO;
import com.pms.authservice.dto.LoginResponseDTO;
import com.pms.authservice.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Controller responsible for handling authentication-related endpoints.
 * <p>
 * Provides an endpoint for user login and token generation.
 * </p>
 *
 * @author Denis Kinyua
 * @since 1.0
 */
@RestController
@AllArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and token generation")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Authenticates a user and generates a JWT token upon successful login.
     *
     * @param loginRequestDTO The login request containing user credentials.
     * @return {@link ResponseEntity} containing the generated token if authentication is successful,
     *         or 401 Unauthorized if authentication fails.
     */
    @Operation(
            summary = "Generate token on user login",
            description = "Authenticates the user with provided credentials and returns a JWT token if successful."
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        Optional<String> token = authenticationService.authenticateUser(loginRequestDTO);
        if(token.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String convertedToken = token.get();
        return ResponseEntity.ok(new LoginResponseDTO(convertedToken));
    }
}