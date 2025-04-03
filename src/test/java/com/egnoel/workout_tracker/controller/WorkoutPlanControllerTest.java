package com.egnoel.workout_tracker.controller;


import com.egnoel.workout_tracker.dto.WorkoutPlanDTO;
import com.egnoel.workout_tracker.entity.User;
import com.egnoel.workout_tracker.entity.WorkoutPlan;
import com.egnoel.workout_tracker.repository.UserRepository;
import com.egnoel.workout_tracker.repository.WorkoutPlanRepository;
import com.egnoel.workout_tracker.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WorkoutPlanControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkoutPlanRepository workoutPlanRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private String userToken;
    private String otherUserToken;
    private User user;
    private User otherUser;

    @BeforeEach
    public void setUp() {
        workoutPlanRepository.deleteAll();
        userRepository.deleteAll();

        user = new User();
        user.setFirstName("Test");
        user.setLastName("user");
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("test123"));
        userRepository.save(user);

        otherUser = new User();
        user.setFirstName("Test");
        user.setLastName("user");
        otherUser.setEmail("other@example.com");
        otherUser.setPassword(passwordEncoder.encode("other123"));
        userRepository.save(otherUser);

        userToken = jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER"))));
        otherUserToken = jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(
                otherUser.getEmail(), otherUser.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER"))));
    }

    @Test
    public void testCreateWorkoutPlanSuccess() throws Exception {
        WorkoutPlanDTO workoutPlanDTO = new WorkoutPlanDTO();
        workoutPlanDTO.setName("My Test Plan");

        mockMvc.perform(post("/api/workout-plans")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workoutPlanDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateWorkoutPlanInvalidData() throws Exception {
        WorkoutPlanDTO workoutPlanDTO = new WorkoutPlanDTO();
        workoutPlanDTO.setName("");

        mockMvc.perform(post("/api/workout-plans")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workoutPlanDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllWorkoutPlansSuccess() throws Exception {
        WorkoutPlan plan = new WorkoutPlan();
        plan.setName("User Plan");
        plan.setUser(user);
        workoutPlanRepository.save(plan);

        mockMvc.perform(get("/api/workout-plans")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetWorkoutPlanByIdAccessDenied() throws Exception {
        WorkoutPlan plan = new WorkoutPlan();
        plan.setName("User Plan");
        plan.setUser(user);
        workoutPlanRepository.save(plan);

        mockMvc.perform(get("/api/workout-plans/" + plan.getId())
                        .header("Authorization", "Bearer " + otherUserToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetWorkoutPlanByIdSuccess() throws Exception {
        WorkoutPlan plan = new WorkoutPlan();
        plan.setName("User Plan");
        plan.setUser(user);
        workoutPlanRepository.save(plan);

        mockMvc.perform(get("/api/workout-plans/" + plan.getId())
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());
    }
}
