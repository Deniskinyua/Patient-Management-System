package com.pms.authservice.controller;

import com.pms.authservice.dto.LoginRequestDTO;
import com.pms.authservice.dto.LoginResponseDTO;
import com.pms.authservice.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Generate token on user login")
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
