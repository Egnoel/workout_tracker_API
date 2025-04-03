/*
package com.egnoel.workout_tracker.seeder;


import com.egnoel.workout_tracker.entity.User;
import com.egnoel.workout_tracker.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UserSeeder implements CommandLineRunner {
    private final UserService userService;

    public UserSeeder(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userService.getAllUsers().isEmpty()) {
            userService.createUser(new User(null, "john","Doe", "john@example.com", "password123", Instant.now(), null, "user"));
            userService.createUser(new User(null, "jane","Smith", "jane@example.com", "password456", Instant.now(), null, "user"));
            System.out.println("Users seeded successfully!");
        }
    }
}
*/