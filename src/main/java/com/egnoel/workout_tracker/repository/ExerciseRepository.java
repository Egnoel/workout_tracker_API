package com.egnoel.workout_tracker.repository;

import com.egnoel.workout_tracker.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
}
