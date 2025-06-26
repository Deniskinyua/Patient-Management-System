package com.pms.authservice.controller;

import com.pms.authservice.dto.LoginRequestDTO;
import com.pms.authservice.dto.LoginResponseDTO;
import com.pms.authservice.model.User;
import com.pms.authservice.repository.UserRepository;
import com.pms.authservice.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

import static org.hibernate.query.sqm.tree.SqmNode.log;

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
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and token generation")
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

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
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO){

        Optional<String> token = authenticationService.authenticateUser(loginRequestDTO);
        if(token.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String convertedToken = token.get();
        return ResponseEntity.ok(new LoginResponseDTO(convertedToken));
    }

    @Operation(summary = "Validate JWT Token")
    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String authorizationHeader){
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            ResponseEntity.status(HttpStatus.UNAUTHORIZED);
        }
        return authenticationService.validateToken(authorizationHeader.substring(7))
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}