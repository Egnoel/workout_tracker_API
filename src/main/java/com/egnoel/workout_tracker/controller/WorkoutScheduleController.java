package com.egnoel.workout_tracker.controller;


import com.egnoel.workout_tracker.dto.WorkoutScheduleDTO;
import com.egnoel.workout_tracker.entity.WorkoutPlan;
import com.egnoel.workout_tracker.entity.WorkoutSchedule;
import com.egnoel.workout_tracker.service.WorkoutPlanService;
import com.egnoel.workout_tracker.service.WorkoutScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/workout-schedules")
@Tag(name = "Workout Schedules", description = "Endpoints for managing workout schedules")
@SecurityRequirement(name = "bearerAuth")
public class WorkoutScheduleController {
    private final WorkoutScheduleService workoutScheduleService;
    private final WorkoutPlanService workoutPlanService;

    public WorkoutScheduleController(WorkoutScheduleService workoutScheduleService,  WorkoutPlanService workoutPlanService) {
        this.workoutScheduleService = workoutScheduleService;
        this.workoutPlanService = workoutPlanService;
    }

    @Operation(summary = "Get all workout schedules", description = "Gets all of the user workout schedules")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workout schedules Received"),
            @ApiResponse(responseCode = "400", description = "Invalid schedule data"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Not the owner or admin")
    })
    @GetMapping
    public ResponseEntity<List<WorkoutScheduleDTO>> getAllWorkoutSchedules() {
        List<WorkoutScheduleDTO> schedules = workoutScheduleService.getAllWorkoutSchedules().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(schedules);
    }

    @Operation(summary = "Get a workout schedule", description = "Gets a single workout schedule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workout schedule Received"),
            @ApiResponse(responseCode = "400", description = "Invalid schedule data"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Not the owner or admin")
    })
    @GetMapping("/{id}")
    public ResponseEntity<WorkoutScheduleDTO> getWorkoutScheduleById(@PathVariable Long id) {
        WorkoutSchedule schedule = workoutScheduleService.getWorkoutScheduleById(id);
        return ResponseEntity.ok(mapToDTO(schedule));
    }

    @Operation(summary = "Get the workout schedule of a user", description = "Gets all the workout plans scheduled for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workout schedule Received"),
            @ApiResponse(responseCode = "400", description = "Invalid schedule data"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Not the owner or admin")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WorkoutScheduleDTO>> getSchedulesByUser(@PathVariable Long userId) {
        List<WorkoutScheduleDTO> schedules = workoutScheduleService.getSchedulesByUser(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(schedules);
    }

    @Operation(summary = "Create a workout schedule", description = "Schedules a workout for a specific plan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workout schedule created"),
            @ApiResponse(responseCode = "400", description = "Invalid schedule data"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Not the owner or admin")
    })
    @PostMapping
    public ResponseEntity<WorkoutScheduleDTO> createWorkoutSchedule(@Valid @RequestBody WorkoutScheduleDTO workoutScheduleDTO,
                                                                    @AuthenticationPrincipal UserDetails userDetails) {
        WorkoutSchedule schedule = new WorkoutSchedule();
        schedule.setScheduledDate(workoutScheduleDTO.getScheduledDate());
        schedule.setCompleted(workoutScheduleDTO.getCompleted());
        schedule.setComments(workoutScheduleDTO.getComments());

        // Busca o WorkoutPlan pelo ID e verifica propriedade
        WorkoutPlan workoutPlan = workoutPlanService.getWorkoutPlanById(workoutScheduleDTO.getWorkoutPlanId());
        if (!workoutPlan.getUser().getEmail().equals(userDetails.getUsername()) &&
                userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        schedule.setWorkoutPlan(workoutPlan);

        WorkoutSchedule savedSchedule = workoutScheduleService.createWorkoutSchedule(schedule);
        return ResponseEntity.ok(mapToDTO(savedSchedule));
    }


    @PutMapping("/{id}")
    public ResponseEntity<WorkoutScheduleDTO> updateWorkoutSchedule(@PathVariable Long id, @RequestBody WorkoutScheduleDTO workoutScheduleDTO) {
        WorkoutSchedule schedule = new WorkoutSchedule();
        schedule.setScheduledDate(workoutScheduleDTO.getScheduledDate());
        schedule.setCompleted(workoutScheduleDTO.getCompleted());
        schedule.setComments(workoutScheduleDTO.getComments());
        WorkoutSchedule updatedSchedule = workoutScheduleService.updateWorkoutSchedule(id, schedule);
        return ResponseEntity.ok(mapToDTO(updatedSchedule));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkoutSchedule(@PathVariable Long id) {
        workoutScheduleService.deleteWorkoutSchedule(id);
        return ResponseEntity.noContent().build();
    }

    private WorkoutScheduleDTO mapToDTO(WorkoutSchedule schedule) {
        WorkoutScheduleDTO dto = new WorkoutScheduleDTO();
        dto.setId(schedule.getId());
        dto.setWorkoutPlanId(schedule.getWorkoutPlan().getId());
        dto.setScheduledDate(schedule.getScheduledDate());
        dto.setCompleted(schedule.getCompleted());
        dto.setComments(schedule.getComments());
        return dto;
    }
}
