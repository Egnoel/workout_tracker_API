package com.egnoel.workout_tracker.service;


import com.egnoel.workout_tracker.dto.UserCreateDTO;
import com.egnoel.workout_tracker.entity.User;
import com.egnoel.workout_tracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signUp(UserCreateDTO userCreateDTO) {
        User user = new User();
        user.setFirstName(userCreateDTO.getFirstName());
        user.setLastName(userCreateDTO.getLastName());
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        if ("admin@hotmail.com".equals(userCreateDTO.getEmail())) {
            user.setRoles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"));
        } else {
            user.setRoles(Collections.singletonList("ROLE_USER"));
        }
        return userRepository.save(user);
    }
}
