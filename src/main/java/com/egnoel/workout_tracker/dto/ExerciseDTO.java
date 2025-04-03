package com.egnoel.workout_tracker.dto;

import com.egnoel.workout_tracker.entity.Category;
import com.egnoel.workout_tracker.entity.MuscleGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ExerciseDTO {
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    private String description;
    private Category category;
    private MuscleGroup muscleGroup;
}
