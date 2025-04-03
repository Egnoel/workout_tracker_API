package com.egnoel.workout_tracker.controller;


import com.egnoel.workout_tracker.dto.ExerciseDTO;
import com.egnoel.workout_tracker.entity.Exercise;
import com.egnoel.workout_tracker.service.ExerciseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exercises")
@Tag(name = "Exercises", description = "Endpoints for managing exercises")
@SecurityRequirement(name = "bearerAuth")
public class ExerciseController {
    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @Operation(summary = "Get all exercises", description = "Returns a list of all exercises")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved exercises"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Missing token")
    })
    @GetMapping
    public ResponseEntity<List<ExerciseDTO>> getAllExercises() {
        List<ExerciseDTO> exercises = exerciseService.getAllExercises().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(exercises);
    }

    @Operation(summary = "Get an exercise", description = "Gets a single exercise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercise Retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid exercise data"),
            @ApiResponse(responseCode = "403", description = "Forbidden 403- Not Owner or Admin")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExerciseDTO> getExerciseById(@PathVariable Long id) {
        Exercise exercise = exerciseService.getExerciseById(id);
        return ResponseEntity.ok(mapToDTO(exercise));
    }

    @Operation(summary = "Create an exercise", description = "Creates a new exercise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercise created"),
            @ApiResponse(responseCode = "400", description = "Invalid exercise data")
    })
    @PostMapping
    public ResponseEntity<ExerciseDTO> createExercise(@RequestBody ExerciseDTO exerciseDTO) {
        Exercise exercise = new Exercise();
        exercise.setName(exerciseDTO.getName());
        exercise.setDescription(exerciseDTO.getDescription());
        exercise.setCategory(exerciseDTO.getCategory());
        exercise.setMuscleGroup(exerciseDTO.getMuscleGroup());
        Exercise savedExercise = exerciseService.createExercise(exercise);
        return ResponseEntity.ok(mapToDTO(savedExercise));
    }

    @Operation(summary = "Updates an exercise", description = "Updates an exercise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercise Updated"),
            @ApiResponse(responseCode = "400", description = "Invalid exercise data"),
            @ApiResponse(responseCode = "403", description = "Forbidden 403- Not Owner or Admin")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ExerciseDTO> updateExercise(@PathVariable Long id, @RequestBody ExerciseDTO exerciseDTO) {
        Exercise exercise = new Exercise();
        exercise.setName(exerciseDTO.getName());
        exercise.setDescription(exerciseDTO.getDescription());
        exercise.setCategory(exerciseDTO.getCategory());
        exercise.setMuscleGroup(exerciseDTO.getMuscleGroup());
        Exercise updatedExercise = exerciseService.updateExercise(id, exercise);
        return ResponseEntity.ok(mapToDTO(updatedExercise));
    }

    @Operation(summary = "Delete an exercise", description = "Deletes an exercise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercise Deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid exercise data"),
            @ApiResponse(responseCode = "403", description = "Forbidden 403- Not Owner or Admin")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }

    private ExerciseDTO mapToDTO(Exercise exercise) {
        ExerciseDTO dto = new ExerciseDTO();
        dto.setId(exercise.getId());
        dto.setName(exercise.getName());
        dto.setDescription(exercise.getDescription());
        dto.setCategory(exercise.getCategory());
        dto.setMuscleGroup(exercise.getMuscleGroup());
        return dto;
    }
}
