package com.egnoel.workout_tracker.repository;

import com.egnoel.workout_tracker.entity.WorkoutSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface WorkoutScheduleRepository extends JpaRepository<WorkoutSchedule, Long> {
    @Query("SELECT ws FROM WorkoutSchedule ws WHERE ws.workoutPlan.user.id = :userId")
    List<WorkoutSchedule> findByUserId(Long userId);
    List<WorkoutSchedule> findByWorkoutPlanIdOrderByScheduledDateAsc(Long workoutPlanId);

    List<WorkoutSchedule> findByWorkoutPlanUserEmailAndCompletedTrueOrderByScheduledDateDesc(String email);

    List<WorkoutSchedule> findByWorkoutPlanUserEmailAndCompletedTrueAndScheduledDateBetweenOrderByScheduledDateDesc(
            String email, Instant startDate, Instant endDate);
}
