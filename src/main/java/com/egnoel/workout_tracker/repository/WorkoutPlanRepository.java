package com.egnoel.workout_tracker.repository;

import com.egnoel.workout_tracker.entity.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {
    List<WorkoutPlan> findByUserEmail(String email);
}
