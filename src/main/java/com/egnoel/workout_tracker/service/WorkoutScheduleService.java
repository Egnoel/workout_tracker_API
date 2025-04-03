package com.egnoel.workout_tracker.service;

import com.egnoel.workout_tracker.entity.WorkoutSchedule;
import com.egnoel.workout_tracker.repository.WorkoutScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class WorkoutScheduleService {
    private final WorkoutScheduleRepository workoutScheduleRepository;
    private final UserService userService;


    public WorkoutScheduleService(WorkoutScheduleRepository workoutScheduleRepository, UserService userService) {
        this.workoutScheduleRepository = workoutScheduleRepository;
        this.userService = userService;
    }

    public WorkoutSchedule createWorkoutSchedule(WorkoutSchedule workoutSchedule) {
        return workoutScheduleRepository.save(workoutSchedule);
    }

    public WorkoutSchedule getWorkoutScheduleById(Long id) {
        return workoutScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkoutSchedule not found with id: " + id));
    }

    public List<WorkoutSchedule> getAllWorkoutSchedules() {
        return workoutScheduleRepository.findAll();
    }

    public WorkoutSchedule updateWorkoutSchedule(Long id, WorkoutSchedule workoutScheduleDetails) {
        WorkoutSchedule workoutSchedule = getWorkoutScheduleById(id);
        workoutSchedule.setScheduledDate(workoutScheduleDetails.getScheduledDate());
        workoutSchedule.setCompleted(workoutScheduleDetails.getCompleted());
        workoutSchedule.setComments(workoutScheduleDetails.getComments());
        return workoutScheduleRepository.save(workoutSchedule);
    }

    public void deleteWorkoutSchedule(Long id) {
        WorkoutSchedule workoutSchedule = getWorkoutScheduleById(id);
        workoutScheduleRepository.delete(workoutSchedule);
    }

    public List<WorkoutSchedule> getSchedulesByUser(Long userId) {
        userService.getUserById(userId);
        return workoutScheduleRepository.findByUserId(userId);
    }

    public List<WorkoutSchedule> getPastWorkoutsByUserEmail(String email, Instant startDate, Instant endDate) {
        if (startDate == null && endDate == null) {
            return workoutScheduleRepository.findByWorkoutPlanUserEmailAndCompletedTrueOrderByScheduledDateDesc(email);
        }
        return workoutScheduleRepository.findByWorkoutPlanUserEmailAndCompletedTrueAndScheduledDateBetweenOrderByScheduledDateDesc(
                email, startDate, endDate);
    }
}
