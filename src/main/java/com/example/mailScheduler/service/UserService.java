package com.example.mailScheduler.service;

import com.example.mailScheduler.config.JwtFilter;
import com.example.mailScheduler.model.ErrorResponse;
import com.example.mailScheduler.model.JwtResponse;
import com.example.mailScheduler.model.User;
import com.example.mailScheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private EmailService emailService;

    // Register user method returning JSON response with appropriate message
    public ResponseEntity<Object> registerUser(String username, String password, String name, String designation, Long phone_Number) {
        if (!emailService.isValidEmail(username)) {
            ErrorResponse errorResponse = new ErrorResponse("Invalid email address! Please enter a valid email.");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }


        if (userRepository.findByUsername(username) != null) {
            ErrorResponse errorResponse = new ErrorResponse("Username already exists!");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setName(name);
        user.setDesignation(designation);
        user.setPhone_Number(phone_Number);

        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully. Pending admin approval.", HttpStatus.OK);
    }


    // Fetch all unapproved users
    public List<User> getUnapprovedUsers() {
        return userRepository.findByIsApprovedFalse();
    }

    // Approve a user by ID
    public boolean approveUser(Long userId) {
        return userRepository.findById(userId).map(user -> {
            user.setApproved(true);
            userRepository.save(user);
            return true;
        }).orElse(false);
    }

    // Login method with JSON response and status code handling
    public ResponseEntity<Object> loginUser(String username, String password) {
        User user = userRepository.findByUsername(username);

        // If user does not exist or is not approved, return error response
        if (user == null || !user.isApproved())
        {
            ErrorResponse errorResponse = new ErrorResponse("Invalid username or user not approved.");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }

        // If password matches, return success response
        if (passwordEncoder.matches(password, user.getPasswordHash()))
        {
            // Generate JWT token
            String token = jwtFilter.generateToken(username); // Generate token with username
            return new ResponseEntity<>(new JwtResponse("Login successful!", token), HttpStatus.OK);
//            ErrorResponse errorResponse = new ErrorResponse("Login successful!");
//            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }

        else
        {
            // If password is incorrect, return error response
            ErrorResponse errorResponse = new ErrorResponse("Incorrect password.");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }
}