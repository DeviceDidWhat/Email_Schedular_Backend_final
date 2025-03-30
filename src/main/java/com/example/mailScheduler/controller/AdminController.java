//package com.example.mailScheduler.controller;
//
//import com.example.mailScheduler.model.ErrorResponse;
//import com.example.mailScheduler.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/admin")
//public class AdminController {
//
//    @Autowired
//    private UserService userService;
//
//    @PostMapping("/approve")
//    public ResponseEntity<Object> approveUser(@RequestParam Long userId) {
//        boolean success = userService.approveUser(userId);
//
//        if (success) {
//            // If approval is successful, return success message as JSON
//            return new ResponseEntity<>("User approved successfully.", HttpStatus.OK);
//        } else {
//            // If approval fails, return error message as JSON
//            return new ResponseEntity<>(new ErrorResponse("User not found."), HttpStatus.BAD_REQUEST);
//        }
//    }
//}
//
package com.example.mailScheduler.controller;

import com.example.mailScheduler.model.ErrorResponse;
import com.example.mailScheduler.model.User;
import com.example.mailScheduler.repository.UserRepository;
import com.example.mailScheduler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender; // JavaMailSender to send emails

    @Autowired
    private UserRepository userRepository;

    private static final String ADMIN_PASSWORD = "Kavya@2135"; // Replace with a secure value
    private static final String ADMIN_EMAIL = "b24cm1036@iitj.ac.in"; // Replace with your admin email

    private String generatedOtp; // Temporarily store OTP for validation


    // Endpoint to validate admin password and send OTP
    @PostMapping("/validate-password")
    public ResponseEntity<Object> validateAdminPassword(@RequestParam String password) {
        if (ADMIN_PASSWORD.equals(password)) {
            generatedOtp = generateOtp();
            sendOtpEmail(ADMIN_EMAIL, generatedOtp);
            return new ResponseEntity<>("OTP sent to admin email.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorResponse("Invalid password."), HttpStatus.UNAUTHORIZED);
        }
    }

    // Helper method to send the OTP email
    private void sendOtpEmail(String recipientEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Admin OTP");
        message.setText("Your OTP for admin access is: " + otp);

        try {
            mailSender.send(message);
            System.out.println("OTP email sent successfully.");
        } catch (Exception e) {
            System.err.println("Error sending OTP email: " + e.getMessage());
        }
    }

    @PostMapping("/deny")
    public ResponseEntity<?> denyUser(@RequestParam Long userId, @RequestParam String password) {
        try {
            // Find the user by ID
            Optional<User> userOptional = userRepository.findById(userId);

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("User not found"));
            }

            User user = userOptional.get();

            // Delete the user from the repository
            userRepository.delete(user);
            // Or alternatively, you can use deleteById
            // userRepository.deleteById(userId);

            return ResponseEntity.ok(new ErrorResponse("User has been denied access"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error processing denial request: " + e.getMessage()));
        }
    }

    // Endpoint to validate the OTP
    @PostMapping("/validate-otp")
    public ResponseEntity<Object> validateOtp(@RequestParam String otp) {
        if (generatedOtp != null && generatedOtp.equals(otp)) {
            return new ResponseEntity<>("OTP validated successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorResponse("Invalid OTP."), HttpStatus.UNAUTHORIZED);
        }
    }

    // Helper method to generate a 6-digit OTP
    private String generateOtp() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

    // Endpoint to get unapproved users
    @GetMapping("/unapproved-users")
    public ResponseEntity<Object> getUnapprovedUsers(@RequestParam String password) {
        if (!ADMIN_PASSWORD.equals(password)) {
            return new ResponseEntity<>(new ErrorResponse("Invalid password."), HttpStatus.UNAUTHORIZED);
        }

        List<User> unapprovedUsers = userService.getUnapprovedUsers();
        return new ResponseEntity<>(unapprovedUsers, HttpStatus.OK);
    }

    // Endpoint to approve a user
    @PostMapping("/approve")
    public ResponseEntity<Object> approveUser(@RequestParam Long userId, @RequestParam String password) {
        if (!ADMIN_PASSWORD.equals(password)) {
            return new ResponseEntity<>(new ErrorResponse("Invalid password."), HttpStatus.UNAUTHORIZED);
        }

        boolean success = userService.approveUser(userId);

        if (success) {
            return new ResponseEntity<>("User approved successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorResponse("User not found."), HttpStatus.BAD_REQUEST);
        }
    }
}