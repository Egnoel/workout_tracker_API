package com.egnoel.workout_tracker.controller;


import com.egnoel.workout_tracker.dto.WorkoutExerciseDTO;
import com.egnoel.workout_tracker.dto.WorkoutPlanDTO;
import com.egnoel.workout_tracker.dto.WorkoutScheduleDTO;
import com.egnoel.workout_tracker.entity.User;
import com.egnoel.workout_tracker.entity.WorkoutExercise;
import com.egnoel.workout_tracker.entity.WorkoutPlan;
import com.egnoel.workout_tracker.entity.WorkoutSchedule;
import com.egnoel.workout_tracker.service.WorkoutPlanService;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/workout-plans")
@Tag(name = "Workout Plans", description = "Endpoints for managing workout plans")
@SecurityRequirement(name = "bearerAuth")
public class WorkoutPlanController {
    private final WorkoutPlanService workoutPlanService;

    public WorkoutPlanController(WorkoutPlanService workoutPlanService) {
        this.workoutPlanService = workoutPlanService;
    }

    @Operation(summary = "Get all workout plans", description = "Returns workout plans for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved workout plans"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Missing token")
    })
    @GetMapping
    public ResponseEntity<List<WorkoutPlanDTO>> getAllWorkoutPlans(@AuthenticationPrincipal UserDetails userDetails) {
        List<WorkoutPlan> plans = workoutPlanService.getWorkoutPlansByUserEmail(userDetails.getUsername());
        return ResponseEntity.ok(plans.stream().map(this::mapToDTO).collect(Collectors.toList()));
    }

    @Operation(summary = "Get workout plan by ID", description = "Returns a specific workout plan if owned by the user or admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workout plan found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Not the owner or admin"),
            @ApiResponse(responseCode = "404", description = "Workout plan not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<WorkoutPlanDTO> getWorkoutPlanById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        WorkoutPlan workoutPlan = workoutPlanService.getWorkoutPlanById(id);
        if (!workoutPlan.getUser().getEmail().equals(userDetails.getUsername()) &&
                userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(mapToDTO(workoutPlan));
    }

    @Operation(summary = "Create a workout plan", description = "Creates a new workout plan for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workout plan created"),
            @ApiResponse(responseCode = "400", description = "Invalid workout plan data")
    })
    @PostMapping
    public ResponseEntity<WorkoutPlanDTO> createWorkoutPlan(@Valid @RequestBody WorkoutPlanDTO workoutPlanDTO,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            System.out.println("User email: " + userDetails.getUsername());
            WorkoutPlan workoutPlan = new WorkoutPlan();
            workoutPlan.setName(workoutPlanDTO.getName());
            User user = workoutPlanService.getUserByEmail(userDetails.getUsername());
            System.out.println("User found: " + user.getEmail());
            workoutPlan.setUser(user);
            WorkoutPlan savedPlan = workoutPlanService.createWorkoutPlan(workoutPlan);
            WorkoutPlanDTO dto = mapToDTO(savedPlan);
            System.out.println("DTO created: " + dto);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            System.err.println("Error creating workout plan: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Update a workout plan", description = "Updates an existing workout plan for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workout plan Updated"),
            @ApiResponse(responseCode = "400", description = "Invalid workout plan data"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Not the owner or admin")
    })
    @PutMapping("/{id}")
    public ResponseEntity<WorkoutPlanDTO> updateWorkoutPlan(@PathVariable Long id,
                                                           @Valid @RequestBody WorkoutPlanDTO workoutPlanDTO,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        WorkoutPlan workoutPlan = workoutPlanService.getWorkoutPlanById(id);
        if (!workoutPlan.getUser().getEmail().equals(userDetails.getUsername()) &&
                userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        workoutPlan.setName(workoutPlanDTO.getName());
        WorkoutPlan updatedPlan = workoutPlanService.updateWorkoutPlan(id, workoutPlan);
        return ResponseEntity.ok(mapToDTO(updatedPlan));
    }

    @Operation(summary = "Delete a workout plan", description = "Deletes a workout plan for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workout plan Deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid workout plan data"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Not the owner or admin")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkoutPlan(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        WorkoutPlan workoutPlan = workoutPlanService.getWorkoutPlanById(id);
        if (!workoutPlan.getUser().getEmail().equals(userDetails.getUsername()) &&
                userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        workoutPlanService.deleteWorkoutPlan(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add exercises to a workout plan", description = "Adds exercises to an existing workout plan for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercise added"),
            @ApiResponse(responseCode = "400", description = "Invalid Exercise data"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Not the owner or admin")
    })
    @PostMapping("/{id}/exercises")
    public ResponseEntity<WorkoutExerciseDTO> addExerciseToWorkoutPlan(@PathVariable Long id,
                                                                       @RequestBody WorkoutExerciseDTO workoutExerciseDTO,
                                                                       @AuthenticationPrincipal UserDetails userDetails) {
        WorkoutPlan workoutPlan = workoutPlanService.getWorkoutPlanById(id);
        if (!workoutPlan.getUser().getEmail().equals(userDetails.getUsername()) &&
                userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        WorkoutExercise workoutExercise = workoutPlanService.addExerciseToWorkoutPlan(
                id, workoutExerciseDTO.getExerciseId(), workoutExerciseDTO.getSets(),
                workoutExerciseDTO.getRepetitions(), workoutExerciseDTO.getWeight());
        return ResponseEntity.ok(mapToWorkoutExerciseDTO(workoutExercise));
    }

    private WorkoutPlanDTO mapToDTO(WorkoutPlan workoutPlan) {
        WorkoutPlanDTO dto = new WorkoutPlanDTO();
        dto.setId(workoutPlan.getId());
        dto.setUserId(workoutPlan.getUser() != null ? workoutPlan.getUser().getId() : null);
        dto.setName(workoutPlan.getName());
        dto.setCreatedAt(workoutPlan.getCreatedAt());
        dto.setUpdatedAt(workoutPlan.getUpdatedAt());
        dto.setExercises(workoutPlan.getWorkoutExercises() != null ?
                workoutPlan.getWorkoutExercises().stream()
                        .map(this::mapToWorkoutExerciseDTO)
                        .collect(Collectors.toList()) : Collections.emptyList());
        dto.setSchedules(workoutPlan.getWorkoutSchedules() != null ?
                workoutPlan.getWorkoutSchedules().stream()
                        .map(this::mapToWorkoutScheduleDTO)
                        .sorted((s1, s2) -> s1.getScheduledDate().compareTo(s2.getScheduledDate()))
                        .collect(Collectors.toList()) : Collections.emptyList());
        return dto;
    }

    private WorkoutScheduleDTO mapToWorkoutScheduleDTO(WorkoutSchedule workoutSchedule) {
        WorkoutScheduleDTO dto = new WorkoutScheduleDTO();
        dto.setId(workoutSchedule.getId());
        dto.setWorkoutPlanId(workoutSchedule.getWorkoutPlan().getId());
        dto.setScheduledDate(workoutSchedule.getScheduledDate());
        dto.setCompleted(workoutSchedule.getCompleted());
        dto.setComments(workoutSchedule.getComments());
        return dto;
    }

    private WorkoutExerciseDTO mapToWorkoutExerciseDTO(WorkoutExercise workoutExercise) {
        WorkoutExerciseDTO dto = new WorkoutExerciseDTO();
        dto.setId(workoutExercise.getId());
        dto.setExerciseId(workoutExercise.getExercise().getId());
        dto.setExerciseName(workoutExercise.getExercise().getName());
        dto.setSets(workoutExercise.getSets());
        dto.setRepetitions(workoutExercise.getRepetitions());
        dto.setWeight(workoutExercise.getWeight());
        return dto;
    }
}
