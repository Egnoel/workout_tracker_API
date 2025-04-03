package com.egnoel.workout_tracker.service;


import com.egnoel.workout_tracker.entity.User;
import com.egnoel.workout_tracker.entity.WorkoutPlan;
import com.egnoel.workout_tracker.repository.UserRepository;
import com.egnoel.workout_tracker.repository.WorkoutPlanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WorkoutPlanServiceTest {
    @Mock
    private WorkoutPlanRepository workoutPlanRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WorkoutPlanService workoutPlanService;

    @Test
    public void testGetWorkoutPlansByUserEmail() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        WorkoutPlan plan = new WorkoutPlan();
        plan.setName("Test Plan");
        plan.setUser(user);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(workoutPlanRepository.findByUserEmail(email)).thenReturn(Collections.singletonList(plan));

        var result = workoutPlanService.getWorkoutPlansByUserEmail(email);
        assertEquals(1, result.size());
        assertEquals("Test Plan", result.get(0).getName());
    }

    @Test
    public void testGetWorkoutPlanByIdNotFound() {
        Long id = 1L;
        when(workoutPlanRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> workoutPlanService.getWorkoutPlanById(id));
    }

    @Test
    public void testCreateWorkoutPlan() {
        WorkoutPlan plan = new WorkoutPlan();
        plan.setName("New Plan");
        when(workoutPlanRepository.save(any(WorkoutPlan.class))).thenReturn(plan);

        WorkoutPlan result = workoutPlanService.createWorkoutPlan(plan);
        assertEquals("New Plan", result.getName());
    }
}
