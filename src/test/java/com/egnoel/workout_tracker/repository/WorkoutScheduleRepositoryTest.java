package com.egnoel.workout_tracker.repository;

import com.egnoel.workout_tracker.entity.User;
import com.egnoel.workout_tracker.entity.WorkoutPlan;
import com.egnoel.workout_tracker.entity.WorkoutSchedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class WorkoutScheduleRepositoryTest {
    @Autowired
    private WorkoutScheduleRepository workoutScheduleRepository;

    @Autowired
    private WorkoutPlanRepository workoutPlanRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        workoutScheduleRepository.deleteAll();
        workoutPlanRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setFirstName("Test");
        user.setLastName("user");
        user.setEmail("test@example.com");
        user.setPassword("encoded");
        userRepository.save(user);

        WorkoutPlan plan = new WorkoutPlan();
        plan.setName("Test Plan");
        plan.setUser(user);
        workoutPlanRepository.save(plan);

        WorkoutSchedule schedule = new WorkoutSchedule();
        schedule.setWorkoutPlan(plan);
        schedule.setScheduledDate(Instant.parse("2025-04-03T14:30:00Z"));
        schedule.setCompleted(true);
        schedule.setComments("Test workout");
        workoutScheduleRepository.save(schedule);
    }

    @Test
    public void testFindByWorkoutPlanUserEmailAndCompletedTrue() {
        List<WorkoutSchedule> schedules = workoutScheduleRepository
                .findByWorkoutPlanUserEmailAndCompletedTrueOrderByScheduledDateDesc("test@example.com");
        assertEquals(1, schedules.size());
        assertEquals("Test workout", schedules.getFirst().getComments());
    }
}
