package com.egnoel.workout_tracker.dto;

import lombok.Data;

@Data
public class WorkoutExerciseDTO {
    private Long id;
    private Long exerciseId;
    private String exerciseName; // Para facilitar no frontend
    private Integer sets;
    private Integer repetitions;
    private Double weight;
}
