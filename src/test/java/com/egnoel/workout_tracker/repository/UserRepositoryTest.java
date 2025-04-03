package com.egnoel.workout_tracker.repository;


import com.egnoel.workout_tracker.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();

        User user = new User();
        user.setFirstName("Test");
        user.setLastName("user");
        user.setEmail("test@example.com");
        user.setPassword("encoded");
        userRepository.save(user);
    }

    @Test
    public void testFindByEmail() {
        Optional<User> user = userRepository.findByEmail("test@example.com");
        assertTrue(user.isPresent());
        assertEquals("Test", user.get().getFirstName());
    }

    @Test
    public void testFindByEmailNotFound() {
        Optional<User> user = userRepository.findByEmail("notfound@example.com");
        assertTrue(user.isEmpty());
    }
}
