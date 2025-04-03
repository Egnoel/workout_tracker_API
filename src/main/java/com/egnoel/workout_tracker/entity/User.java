package com.egnoel.workout_tracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
   private String firstName;
    @Column(nullable = false)
   private String lastName;
    @Column(nullable = false, unique = true)
    @Email
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutPlan> workoutPlans;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private List<String> roles;
}
