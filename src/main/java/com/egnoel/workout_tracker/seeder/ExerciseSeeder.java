package com.egnoel.workout_tracker.seeder;


import com.egnoel.workout_tracker.entity.Category;
import com.egnoel.workout_tracker.entity.Exercise;
import com.egnoel.workout_tracker.entity.Exercise.*;


import com.egnoel.workout_tracker.entity.MuscleGroup;
import com.egnoel.workout_tracker.service.ExerciseService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ExerciseSeeder implements CommandLineRunner {
    private final ExerciseService exerciseService;

    public ExerciseSeeder(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (exerciseService.getAllExercises().isEmpty()) {
            exerciseService.createExercise(new Exercise(null, "Bench Press", "Chest exercise with barbell", Category.STRENGTH, MuscleGroup.CHEST, null));
            exerciseService.createExercise(new Exercise(null, "Running", "Outdoor cardio", Category.CARDIO, MuscleGroup.LEGS, null));
            exerciseService.createExercise(new Exercise(null, "Plank", "Core stability", Category.BODYWEIGHT, MuscleGroup.CORE, null));
            exerciseService.createExercise(new Exercise(null, "Squat", "Leg exercise", Category.STRENGTH, MuscleGroup.LEGS, null));
            System.out.println("Exercises seeded successfully!");
        }
    }
}
