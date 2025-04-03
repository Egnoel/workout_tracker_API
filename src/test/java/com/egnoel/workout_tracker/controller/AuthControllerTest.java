package com.egnoel.workout_tracker.controller;


import com.egnoel.workout_tracker.dto.UserCreateDTO;
import com.egnoel.workout_tracker.entity.User;
import com.egnoel.workout_tracker.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testSignUpSuccess() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setFirstName("test");
        userCreateDTO.setLastName("user");
        userCreateDTO.setEmail("test@example.com");
        userCreateDTO.setPassword("test123");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void testSignUpInvalidData() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setFirstName("");
        userCreateDTO.setLastName("");
        userCreateDTO.setEmail("invalid-email");
        userCreateDTO.setPassword("");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginSuccess() throws Exception {
        User user = new User();
        user.setFirstName("test");
        user.setLastName("user");
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("test123"));
        userRepository.save(user);

        UserCreateDTO loginRequest = new UserCreateDTO();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("test123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoginInvalidCredentials() throws Exception {
        UserCreateDTO loginRequest = new UserCreateDTO();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}
