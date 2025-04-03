package com.egnoel.workout_tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class WorkoutScheduleDTO {
    private Long id;
    @NotNull
    @NotBlank
    private Long workoutPlanId;
    @NotNull
    @NotBlank
    private Instant scheduledDate;
    @NotNull
    private Boolean completed;
    private String comments;
}
