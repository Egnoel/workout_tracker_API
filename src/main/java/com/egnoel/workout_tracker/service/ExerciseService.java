package com.egnoel.workout_tracker.service;

import com.egnoel.workout_tracker.entity.Exercise;
import com.egnoel.workout_tracker.repository.ExerciseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public Exercise createExercise(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    public Exercise getExerciseById(Long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exercise not found with id: " + id));
    }

    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    public Exercise updateExercise(Long id, Exercise exerciseDetails) {
        Exercise exercise = getExerciseById(id);
        exercise.setName(exerciseDetails.getName());
        exercise.setDescription(exerciseDetails.getDescription());
        exercise.setCategory(exerciseDetails.getCategory());
        exercise.setMuscleGroup(exerciseDetails.getMuscleGroup());
        return exerciseRepository.save(exercise);
    }

    public void deleteExercise(Long id) {
        Exercise exercise = getExerciseById(id);
        exerciseRepository.delete(exercise);
    }
}
