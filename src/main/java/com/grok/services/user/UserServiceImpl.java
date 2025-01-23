package com.grok.services.user;

import com.grok.exceptions.ResourceNotFoundException;
import com.grok.repository.UserRepository;
import com.grok.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user){
        user.getExpenses().forEach(expense -> {
            expense.setUser(user);
        });
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No record found to update for id: " + userId));
    }

    @Override
    public User updateUserById(Integer userId, User userDetails) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if (userDetails.getTenant() != null) {
            user.setTenant(userDetails.getTenant());
        }

        if (userDetails.getEmail() != null && !userDetails.getEmail().isEmpty()) {
            user.setEmail(userDetails.getEmail());
        }

        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(userDetails.getPassword());
        }

        if (userDetails.getRole() != null && !userDetails.getRole().isEmpty()) {
            user.setRole(userDetails.getRole());
        }

        if (userDetails.getExpenses() != null && !userDetails.getExpenses().isEmpty()) {
            userDetails.getExpenses().forEach(expense -> expense.setUser(user));
            user.getExpenses().addAll(userDetails.getExpenses());
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(Integer userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No record found to update for id: " + userId));
        userRepository.deleteById(userId);
    }

}