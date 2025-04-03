package com.egnoel.workout_tracker.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class WorkoutPlanDTO {
    private Long id;
    private Long userId;
    @NotNull
    @NotBlank
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
    private List<WorkoutExerciseDTO> exercises;
    private List<WorkoutScheduleDTO> schedules;
}
