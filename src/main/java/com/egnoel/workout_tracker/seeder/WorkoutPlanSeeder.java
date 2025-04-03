package com.egnoel.workout_tracker.seeder;


import com.egnoel.workout_tracker.entity.User;
import com.egnoel.workout_tracker.entity.WorkoutPlan;
import com.egnoel.workout_tracker.service.UserService;
import com.egnoel.workout_tracker.service.WorkoutPlanService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class WorkoutPlanSeeder implements CommandLineRunner {
    private final WorkoutPlanService workoutPlanService;
    private final UserService userService;

    public WorkoutPlanSeeder(WorkoutPlanService workoutPlanService, UserService userService) {
        this.workoutPlanService = workoutPlanService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (workoutPlanService.getAllWorkoutPlans().isEmpty()) {
            User john = userService.getUserById(1L); // ID 1 do UserSeeder
            User jane = userService.getUserById(2L); // ID 2 do UserSeeder

            workoutPlanService.createWorkoutPlan(new WorkoutPlan(null, john, "Chest Day", Instant.now(), null, null, null));
            workoutPlanService.createWorkoutPlan(new WorkoutPlan(null, jane, "Leg Day", Instant.now(), null, null, null));
            System.out.println("WorkoutPlans seeded successfully!");
        }
    }
}
