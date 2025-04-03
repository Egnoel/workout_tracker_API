package com.egnoel.workout_tracker.controller;


import com.egnoel.workout_tracker.entity.User;
import com.egnoel.workout_tracker.entity.WorkoutPlan;
import com.egnoel.workout_tracker.entity.WorkoutSchedule;
import com.egnoel.workout_tracker.repository.UserRepository;
import com.egnoel.workout_tracker.repository.WorkoutPlanRepository;
import com.egnoel.workout_tracker.repository.WorkoutScheduleRepository;
import com.egnoel.workout_tracker.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WorkoutReportControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkoutPlanRepository workoutPlanRepository;

    @Autowired
    private WorkoutScheduleRepository workoutScheduleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    private String token;

    @BeforeEach
    public void setUp() {
        workoutScheduleRepository.deleteAll();
        workoutPlanRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setFirstName("Test");
        user.setLastName("user");
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("test123"));
        userRepository.save(user);

        token = jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER"))));

        WorkoutPlan plan = new WorkoutPlan();
        plan.setName("Test Plan");
        plan.setUser(user);
        workoutPlanRepository.save(plan);

        WorkoutSchedule schedule = new WorkoutSchedule();
        schedule.setWorkoutPlan(plan);
        schedule.setScheduledDate(Instant.parse("2025-04-03T14:30:00Z"));
        schedule.setCompleted(true);
        schedule.setComments("Test workout");
        workoutScheduleRepository.save(schedule);
    }

    @Test
    public void testGetPastWorkoutsSuccess() throws Exception {
        mockMvc.perform(get("/api/reports/past-workouts")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPastWorkoutsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/reports/past-workouts"))
                .andExpect(status().isUnauthorized());
    }
}
