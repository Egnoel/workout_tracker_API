package com.egnoel.workout_tracker.seeder;


import com.egnoel.workout_tracker.entity.WorkoutPlan;
import com.egnoel.workout_tracker.entity.WorkoutSchedule;
import com.egnoel.workout_tracker.service.WorkoutPlanService;
import com.egnoel.workout_tracker.service.WorkoutScheduleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class WorkoutScheduleSeeder implements CommandLineRunner {
    private final WorkoutScheduleService workoutScheduleService;
    private final WorkoutPlanService workoutPlanService;

    public WorkoutScheduleSeeder(WorkoutScheduleService workoutScheduleService,
                                 WorkoutPlanService workoutPlanService) {
        this.workoutScheduleService = workoutScheduleService;
        this.workoutPlanService = workoutPlanService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (workoutScheduleService.getAllWorkoutSchedules().isEmpty()) {
            WorkoutPlan chestDay = workoutPlanService.getWorkoutPlanById(1L); // "Chest Day"
            WorkoutPlan legDay = workoutPlanService.getWorkoutPlanById(2L);   // "Leg Day"

            workoutScheduleService.createWorkoutSchedule(new WorkoutSchedule(null, chestDay, Instant.parse("2025-04-07T10:00:00Z"), false, "Morning workout"));
            workoutScheduleService.createWorkoutSchedule(new WorkoutSchedule(null, legDay, Instant.parse("2025-04-08T15:00:00Z"), false, "Afternoon workout"));
            System.out.println("WorkoutSchedules seeded successfully!");
        }
    }
}
