package com.egnoel.workout_tracker.dto;


import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class WorkoutReportDTO {
    private Long workoutPlanId;
    private String workoutPlanName;
    private Instant scheduledDate;
    private boolean completed;
    private String comments;
    private List<WorkoutExerciseDTO> exercises;
}
