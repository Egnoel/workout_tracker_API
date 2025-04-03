package com.egnoel.workout_tracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "workout_schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "workout_plan_id", nullable = false)
    private WorkoutPlan workoutPlan;
    @Column(name = "scheduled_date", nullable = false)
    private Instant scheduledDate;
    @Column(nullable = false)
    private Boolean completed = false;
    @Column
    private String comments;
}
