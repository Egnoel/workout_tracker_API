package com.egnoel.workout_tracker.service;


import com.egnoel.workout_tracker.entity.WorkoutSchedule;
import com.egnoel.workout_tracker.repository.WorkoutScheduleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WorkoutScheduleServiceTest {
    @Mock
    private WorkoutScheduleRepository workoutScheduleRepository;

    @Mock
    private WorkoutPlanService workoutPlanService;

    @InjectMocks
    private WorkoutScheduleService workoutScheduleService;

    @Test
    public void testGetPastWorkoutsByUserEmail() {
        String email = "test@example.com";
        Instant startDate = Instant.parse("2025-04-01T00:00:00Z");
        Instant endDate = Instant.parse("2025-04-05T23:59:59Z");

        WorkoutSchedule schedule = new WorkoutSchedule();
        schedule.setScheduledDate(Instant.parse("2025-04-03T14:30:00Z"));
        schedule.setCompleted(true);

        when(workoutScheduleRepository.findByWorkoutPlanUserEmailAndCompletedTrueAndScheduledDateBetweenOrderByScheduledDateDesc(
                email, startDate, endDate)).thenReturn(Collections.singletonList(schedule));

        List<WorkoutSchedule> result = workoutScheduleService.getPastWorkoutsByUserEmail(email, startDate, endDate);
        assertEquals(1, result.size());
        assertEquals(schedule, result.getFirst());
    }

    @Test
    public void testGetPastWorkoutsByUserEmailNoDateRange() {
        String email = "test@example.com";
        WorkoutSchedule schedule = new WorkoutSchedule();
        schedule.setScheduledDate(Instant.parse("2025-04-03T14:30:00Z"));
        schedule.setCompleted(true);

        when(workoutScheduleRepository.findByWorkoutPlanUserEmailAndCompletedTrueOrderByScheduledDateDesc(email))
                .thenReturn(Collections.singletonList(schedule));

        List<WorkoutSchedule> result = workoutScheduleService.getPastWorkoutsByUserEmail(email, null, null);
        assertEquals(1, result.size());
        assertEquals(schedule, result.getFirst());
    }
}
