package com.pms.authservice.services;

import com.pms.authservice.model.User;
import com.pms.authservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    public Optional<User> findByEmail(String email){
        //Impelement any business logic here

        return userRepository.findByEmail(email);

    }
}
