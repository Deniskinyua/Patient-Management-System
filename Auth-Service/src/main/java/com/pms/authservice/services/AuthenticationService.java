package com.pms.authservice.services;

import com.pms.authservice.dto.LoginRequestDTO;
import com.pms.authservice.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Optional<String> authenticateUser(LoginRequestDTO loginRequestDTO) {
        Optional<String> token = userService
                .findByEmail(loginRequestDTO.getEmail())
                .filter(p ->passwordEncoder.matches(loginRequestDTO.getPassword(),
                        p.getPassword()))
                .map(u ->jwtUtil.generateToken(u.getEmail(), u.getRole()));

        return token;
    }
}
