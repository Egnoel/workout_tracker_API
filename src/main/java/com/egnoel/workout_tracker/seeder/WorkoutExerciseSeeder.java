package com.egnoel.workout_tracker.seeder;

import com.egnoel.workout_tracker.entity.WorkoutPlan;
import com.egnoel.workout_tracker.service.WorkoutPlanService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class WorkoutExerciseSeeder implements CommandLineRunner {
    private final WorkoutPlanService workoutPlanService;

    public WorkoutExerciseSeeder(WorkoutPlanService workoutPlanService) {
        this.workoutPlanService = workoutPlanService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!workoutPlanService.hasExercises(1L)) {
            WorkoutPlan chestDay = workoutPlanService.getWorkoutPlanById(1L); // "Chest Day"
            WorkoutPlan legDay = workoutPlanService.getWorkoutPlanById(2L);   // "Leg Day"

            workoutPlanService.addExerciseToWorkoutPlan(chestDay.getId(), 1L, 3, 10, 50.0); // Bench Press
            workoutPlanService.addExerciseToWorkoutPlan(legDay.getId(), 4L, 4, 12, 60.0);   // Squat
            workoutPlanService.addExerciseToWorkoutPlan(legDay.getId(), 2L, 1, 30, null);    // Running (30 min)
            System.out.println("WorkoutExercises seeded successfully!");
        }
    }
}

