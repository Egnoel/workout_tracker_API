package com.egnoel.workout_tracker.service;

import com.egnoel.workout_tracker.entity.User;
import com.egnoel.workout_tracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
      return  userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        if(userDetails.getFirstName()!=null)
            user.setFirstName(userDetails.getFirstName());
        if(userDetails.getLastName()!=null)
            user.setLastName(userDetails.getLastName());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

}
