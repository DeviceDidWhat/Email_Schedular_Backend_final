package com.example.mailScheduler.controller;

import com.example.mailScheduler.model.ErrorResponse;
import com.example.mailScheduler.model.LoginRequest;
import com.example.mailScheduler.model.RegisterRequest;
import com.example.mailScheduler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // Register user method returning JSON response
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody RegisterRequest request) {
        return userService.registerUser(
                request.getUsername(),
                request.getPassword(),
                request.getName(),
                request.getDesignation(),
                request.getPhone_Number()
        );
    }

    // Login user method returning JSON response
    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody LoginRequest request) {
        // Call the loginUser method which already returns ResponseEntity
        return userService.loginUser(request.getUsername(), request.getPassword());
    }
}