package com.example.mailScheduler.controller;

import com.example.mailScheduler.model.*;
import com.example.mailScheduler.repository.CurrentNameRepository;
import com.example.mailScheduler.repository.UserRepository;
import com.example.mailScheduler.service.CurrentNameService;
import com.example.mailScheduler.service.EmailService;
import com.example.mailScheduler.service.FollowUpEmailService;
import com.example.mailScheduler.service.UserService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/emails")

public class EmailController
{

    @Autowired
    private EmailService emailService;

    @Autowired
    private FollowUpEmailService followUpEmailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrentNameService currentNameService;

    @PostMapping("/schedule")

    public ResponseEntity<ErrorResponse> scheduleEmail(@RequestHeader("username") String username ,@RequestBody ScheduledEmail email) {
        try {

//          Fetch the user and check if they are approved
            User user = userRepository.findByUsername(username);
                if (user == null)
                {
                    ErrorResponse errorResponse = new ErrorResponse("User not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
                }
            if (!user.isApproved())
            {
                ErrorResponse errorResponse = new ErrorResponse("User not approved. Please contact the admin.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
            email.setDesignation(user.getDesignation());
            email.setPhone_Number(user.getPhone_Number());
            email.setName(user.getName());

            currentNameService.updateUsername(username);

            // Call the service to schedule the email and handle the response
            ResponseEntity<?> response = emailService.scheduleEmail(email);
            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(new ErrorResponse("Email scheduled successfully"));
            } else {
                // If the response from the service is not successful, return the same error
                return ResponseEntity.status(response.getStatusCode()).body((ErrorResponse) response.getBody());
            }

        } catch (IllegalArgumentException e) {
            // Store the failed email in the database with an error message
            email.setStatus("FAILED");
            email.setErrorMessage(e.getMessage());
            emailService.saveFailedEmail(email);
            return ResponseEntity.badRequest().body(new ErrorResponse("Error: " + e.getMessage()));

        } catch (Exception e) {
            // Store the failed email in the database with an error message
            email.setStatus("FAILED");
            email.setErrorMessage(e.getMessage());
            emailService.saveFailedEmail(email);
            return ResponseEntity.status(500).body(new ErrorResponse("An error occurred: " + e.getMessage()));
        }
    }

    @PostMapping("/followupschedule")

    public ResponseEntity<ErrorResponse> scheduleEmail(@RequestHeader("username") String username , @RequestBody FollowUpScheduledEmail email) {
        try {

//          Fetch the user and check if they are approved
            User user = userRepository.findByUsername(username);
            if (user == null)
            {
                ErrorResponse errorResponse = new ErrorResponse("User not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            if (!user.isApproved())
            {
                ErrorResponse errorResponse = new ErrorResponse("User not approved. Please contact the admin.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
            email.setDesignation(user.getDesignation());
            email.setPhone_Number(user.getPhone_Number());
            email.setName(user.getName());

            currentNameService.updateUsername(username);

            // Call the service to schedule the email and handle the response
            ResponseEntity<?> response = followUpEmailService.scheduleEmail(email);
            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(new ErrorResponse("Email scheduled successfully"));
            } else {
                // If the response from the service is not successful, return the same error
                return ResponseEntity.status(response.getStatusCode()).body((ErrorResponse) response.getBody());
            }

        } catch (IllegalArgumentException e) {
            // Store the failed email in the database with an error message
            email.setStatus("FAILED");
            email.setErrorMessage(e.getMessage());
            followUpEmailService.saveFailedEmail(email);
            return ResponseEntity.badRequest().body(new ErrorResponse("Error: " + e.getMessage()));

        } catch (Exception e) {
            // Store the failed email in the database with an error message
            email.setStatus("FAILED");
            email.setErrorMessage(e.getMessage());
            followUpEmailService.saveFailedEmail(email);
            return ResponseEntity.status(500).body(new ErrorResponse("An error occurred: " + e.getMessage()));
        }
    }

    @PostMapping("/schedule/bulk")
    public ResponseEntity<ErrorResponse> scheduleEmailsInBulk(@RequestHeader("username") String username ,@RequestParam("file") MultipartFile file) {
        try {

            User user = userRepository.findByUsername(username);
            if (user == null)
            {
                ErrorResponse errorResponse = new ErrorResponse("User not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            if (!user.isApproved())
            {
                ErrorResponse errorResponse = new ErrorResponse("User not approved. Please contact the admin.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
            currentNameService.updateUsername(username);

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("No file uploaded"));
            }

            // Process the file
            ResponseEntity<?> response = emailService.scheduleEmailsInBulk(file);
            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(new ErrorResponse("Bulk email scheduling successful"));
            } else {
                return ResponseEntity.status(response.getStatusCode()).body((ErrorResponse) response.getBody());
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("An error occurred: " + e.getMessage()));
        }
    }

    @PostMapping("/followupschedule/bulk")
    public ResponseEntity<ErrorResponse> scheduleEmailsInBulks(@RequestHeader("username") String username ,@RequestParam("file") MultipartFile file) {
        try {

            User user = userRepository.findByUsername(username);
            if (user == null)
            {
                ErrorResponse errorResponse = new ErrorResponse("User not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            if (!user.isApproved())
            {
                ErrorResponse errorResponse = new ErrorResponse("User not approved. Please contact the admin.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
            currentNameService.updateUsername(username);

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("No file uploaded"));
            }

            // Process the file
            ResponseEntity<?> response = followUpEmailService.scheduleEmailsInBulks(file);
            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(new ErrorResponse("Bulk email scheduling successful"));
            } else {
                return ResponseEntity.status(response.getStatusCode()).body((ErrorResponse) response.getBody());
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("An error occurred: " + e.getMessage()));
        }
    }
}