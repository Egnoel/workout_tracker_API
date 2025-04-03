package com.egnoel.workout_tracker.controller;


import com.egnoel.workout_tracker.dto.WorkoutExerciseDTO;
import com.egnoel.workout_tracker.dto.WorkoutReportDTO;
import com.egnoel.workout_tracker.entity.WorkoutSchedule;
import com.egnoel.workout_tracker.service.WorkoutScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "Endpoints for generating workout reports")
@SecurityRequirement(name = "bearerAuth")
public class WorkoutReportController {
    private final WorkoutScheduleService workoutScheduleService;

    public WorkoutReportController(WorkoutScheduleService workoutScheduleService) {
        this.workoutScheduleService = workoutScheduleService;
    }

    @Operation(summary = "Get past workout reports", description = "Returns a list of completed workouts for the authenticated user, optionally filtered by date range.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved past workouts"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    @GetMapping("/past-workouts")
    public ResponseEntity<List<WorkoutReportDTO>> getPastWorkouts(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate) {
        List<WorkoutSchedule> schedules = workoutScheduleService.getPastWorkoutsByUserEmail(userDetails.getUsername(), startDate, endDate);
        List<WorkoutReportDTO> report = schedules.stream().map(this::mapToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(report);
    }

    private WorkoutReportDTO mapToDTO(WorkoutSchedule schedule) {
        WorkoutReportDTO dto = new WorkoutReportDTO();
        dto.setWorkoutPlanId(schedule.getWorkoutPlan().getId());
        dto.setWorkoutPlanName(schedule.getWorkoutPlan().getName());
        dto.setScheduledDate(schedule.getScheduledDate());
        dto.setCompleted(schedule.getCompleted());
        dto.setComments(schedule.getComments());
        dto.setExercises(schedule.getWorkoutPlan().getWorkoutExercises().stream()
                .map(ex -> {
                    WorkoutExerciseDTO exDto = new WorkoutExerciseDTO();
                    exDto.setId(ex.getId());
                    exDto.setExerciseId(ex.getExercise().getId());
                    exDto.setExerciseName(ex.getExercise().getName());
                    exDto.setSets(ex.getSets());
                    exDto.setRepetitions(ex.getRepetitions());
                    exDto.setWeight(ex.getWeight());
                    return exDto;
                })
                .collect(Collectors.toList()));
        return dto;
    }
}
