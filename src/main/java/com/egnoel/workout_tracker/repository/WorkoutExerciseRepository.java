package com.egnoel.workout_tracker.repository;

import com.egnoel.workout_tracker.entity.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, Long> {
}
