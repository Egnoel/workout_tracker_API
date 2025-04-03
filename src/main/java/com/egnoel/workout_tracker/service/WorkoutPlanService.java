package com.egnoel.workout_tracker.service;

import com.egnoel.workout_tracker.entity.Exercise;
import com.egnoel.workout_tracker.entity.User;
import com.egnoel.workout_tracker.entity.WorkoutExercise;
import com.egnoel.workout_tracker.entity.WorkoutPlan;
import com.egnoel.workout_tracker.repository.UserRepository;
import com.egnoel.workout_tracker.repository.WorkoutExerciseRepository;
import com.egnoel.workout_tracker.repository.WorkoutPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkoutPlanService {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final ExerciseService exerciseService;
    private final UserRepository userRepository;

    public WorkoutPlanService(WorkoutPlanRepository workoutPlanRepository,
                              WorkoutExerciseRepository workoutExerciseRepository,
                              ExerciseService exerciseService, UserRepository userRepository) {
        this.workoutPlanRepository = workoutPlanRepository;
        this.workoutExerciseRepository = workoutExerciseRepository;
        this.exerciseService = exerciseService;
        this.userRepository = userRepository;
    }

    public WorkoutPlan createWorkoutPlan(WorkoutPlan workoutPlan) {
        return workoutPlanRepository.save(workoutPlan);
    }

    public WorkoutPlan getWorkoutPlanById(Long id) {
        return workoutPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkoutPlan not found with id: " + id));
    }

    public List<WorkoutPlan> getAllWorkoutPlans() {
        return workoutPlanRepository.findAll();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public List<WorkoutPlan> getWorkoutPlansByUserEmail(String email) {
        return workoutPlanRepository.findByUserEmail(email);
    }

    public WorkoutPlan updateWorkoutPlan(Long id, WorkoutPlan workoutPlanDetails) {
        WorkoutPlan workoutPlan = getWorkoutPlanById(id);
        workoutPlan.setName(workoutPlanDetails.getName());
        workoutPlan.setUpdatedAt(workoutPlanDetails.getUpdatedAt());
        return workoutPlanRepository.save(workoutPlan);
    }

    public void deleteWorkoutPlan(Long id) {
        WorkoutPlan workoutPlan = getWorkoutPlanById(id);
        workoutPlanRepository.delete(workoutPlan);
    }

    @Transactional(readOnly = true)
    public boolean hasExercises(Long workoutPlanId) {
        WorkoutPlan workoutPlan = getWorkoutPlanById(workoutPlanId);
        return !workoutPlan.getWorkoutExercises().isEmpty();
    }

    public WorkoutExercise addExerciseToWorkoutPlan(Long workoutPlanId, Long exerciseId, int sets, int repetitions, Double weight) {
        WorkoutPlan workoutPlan = getWorkoutPlanById(workoutPlanId);
        Exercise exercise = exerciseService.getExerciseById(exerciseId);

        WorkoutExercise workoutExercise = new WorkoutExercise();
        workoutExercise.setWorkoutPlan(workoutPlan);
        workoutExercise.setExercise(exercise);
        workoutExercise.setSets(sets);
        workoutExercise.setRepetitions(repetitions);
        workoutExercise.setWeight(weight);

        return workoutExerciseRepository.save(workoutExercise);
    }
}
