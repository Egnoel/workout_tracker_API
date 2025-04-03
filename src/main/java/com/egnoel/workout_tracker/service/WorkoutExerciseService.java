package com.egnoel.workout_tracker.service;

import com.egnoel.workout_tracker.entity.WorkoutExercise;
import com.egnoel.workout_tracker.repository.WorkoutExerciseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutExerciseService {
    private final WorkoutExerciseRepository workoutExerciseRepository;

    public WorkoutExerciseService(WorkoutExerciseRepository workoutExerciseRepository) {
        this.workoutExerciseRepository = workoutExerciseRepository;
    }

    public WorkoutExercise createWorkoutExercise(WorkoutExercise workoutExercise) {
        return workoutExerciseRepository.save(workoutExercise);
    }

    public WorkoutExercise getWorkoutExerciseById(Long id) {
        return workoutExerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkoutExercise not found with id: " + id));
    }

    public List<WorkoutExercise> getAllWorkoutExercises() {
        return workoutExerciseRepository.findAll();
    }

    public WorkoutExercise updateWorkoutExercise(Long id, WorkoutExercise workoutExerciseDetails) {
        WorkoutExercise workoutExercise = getWorkoutExerciseById(id);
        workoutExercise.setSets(workoutExerciseDetails.getSets());
        workoutExercise.setRepetitions(workoutExerciseDetails.getRepetitions());
        workoutExercise.setWeight(workoutExerciseDetails.getWeight());
        return workoutExerciseRepository.save(workoutExercise);
    }

    public void deleteWorkoutExercise(Long id) {
        WorkoutExercise workoutExercise = getWorkoutExerciseById(id);
        workoutExerciseRepository.delete(workoutExercise);
    }
}
