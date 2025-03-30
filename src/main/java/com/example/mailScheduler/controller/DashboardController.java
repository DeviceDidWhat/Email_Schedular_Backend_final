package com.example.mailScheduler.controller;

import com.example.mailScheduler.model.*;
import com.example.mailScheduler.repository.*;
import com.example.mailScheduler.service.EmailService;
import com.example.mailScheduler.service.FollowUpEmailService;
import com.example.mailScheduler.service.CurrentNameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
public class DashboardController {

    @Autowired
    private EmailSummaryRepository emailSummaryRepository;

    @Autowired
    private ScheduledEmailRepository scheduledEmailRepository;

    @Autowired
    private SentEmailsRepository sentEmailsRepository;

    @Autowired
    private FailedEmailsRepository failedEmailsRepository;

    @Autowired
    private FollowUpScheduledEmailRepository followUpScheduledEmailRepository;

    @Autowired
    private FollowUpSentEmailRepository followUpSentEmailRepository;

    @Autowired
    private FollowUpFailedEmailRepository followUpFailedEmailRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private FollowUpEmailService followUpEmailService;

    @Autowired
    private CurrentNameService currentNameService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardSummary() {
        // Fetching email summary (already existing in your system)
        EmailSummary summary = emailSummaryRepository.findFirstByOrderById()
                .orElse(new EmailSummary());

        // Getting current time and the time ranges for today and yesterday
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay(); // Start of today
        LocalDateTime startOfYesterday = startOfDay.minusDays(1); // Start of yesterday

        // Fetching sent email statistics for today and yesterday
        long sentToday = countSentEmails(startOfDay, now);
        long sentYesterday = countSentEmails(startOfYesterday, startOfDay);

        // Fetching failed email statistics for today and yesterday
        long failedToday = countFailedEmails(startOfDay, now);
        long failedYesterday = countFailedEmails(startOfYesterday, startOfDay);

        // Fetching scheduled email statistics for today and yesterday
        long scheduledToday = countScheduledEmails(startOfDay, now);
        long scheduledYesterday = countScheduledEmails(startOfYesterday, startOfDay);

        // Creating the response map
        Map<String, Object> response = new HashMap<>();
        response.put("totalSent", countTotalEmails(summary.getTotalSent()));
        response.put("totalFailed", countTotalEmails(summary.getTotalFailed()));
        response.put("totalScheduled", countTotalEmails(summary.getTotalScheduled()));
        response.put("sentToday", sentToday);
        response.put("sentYesterday", sentYesterday);
        response.put("failedToday", failedToday);
        response.put("failedYesterday", failedYesterday);
        response.put("scheduledToday", scheduledToday);
        response.put("scheduledYesterday", scheduledYesterday);

        // Add the combined email list data
        response.put("combinedEmails", getCombinedEmailsList());

        // Returning the response
        return ResponseEntity.ok(response);
    }

    // New endpoint to get just the combined emails list
    @GetMapping("/combined-emails")
    public ResponseEntity<List<Map<String, Object>>> getCombinedEmailsListEndpoint() {
        return ResponseEntity.ok(getCombinedEmailsList());
    }

    // Helper method to get combined emails from all repositories
    private List<Map<String, Object>> getCombinedEmailsList() {
        List<Map<String, Object>> combinedList = new ArrayList<>();

        // Get all scheduled emails
        Iterable<ScheduledEmail> scheduledEmails = scheduledEmailRepository.findAll();
        for (ScheduledEmail email : scheduledEmails) {
            Map<String, Object> emailMap = new HashMap<>();
            emailMap.put("id", email.getId());
            emailMap.put("recipient", email.getRecipient());
            emailMap.put("scheduledTime", email.getScheduledTime());
            emailMap.put("company", email.getCompany());
            emailMap.put("status", email.getStatus());
            emailMap.put("source", "Scheduled");
            emailMap.put("type", "Invitation");
            combinedList.add(emailMap);
        }

        // Get all sent emails
        Iterable<SentEmails> sentEmails = sentEmailsRepository.findAll();
        for (SentEmails email : sentEmails) {
            Map<String, Object> emailMap = new HashMap<>();
            emailMap.put("id", email.getId());
            emailMap.put("recipient", email.getRecipient());
            emailMap.put("scheduledTime", email.getScheduledTime());
            emailMap.put("company", email.getCompany());
            emailMap.put("status", email.getStatus());
            emailMap.put("source", "Sent");
            emailMap.put("type", "Invitation");
            combinedList.add(emailMap);
        }

        // Get all failed emails
        Iterable<FailedEmails> failedEmails = failedEmailsRepository.findAll();
        for (FailedEmails email : failedEmails) {
            Map<String, Object> emailMap = new HashMap<>();
            emailMap.put("id", email.getId());
            emailMap.put("recipient", email.getRecipient());
            emailMap.put("scheduledTime", email.getScheduledTime());
            emailMap.put("company", email.getCompany());
            emailMap.put("status", email.getStatus());
            emailMap.put("source", "Failed");
            emailMap.put("type", "Invitation");
            combinedList.add(emailMap);
        }

        // Get all follow-up scheduled emails
        Iterable<FollowUpScheduledEmail> followUpScheduledEmails = followUpScheduledEmailRepository.findAll();
        for (FollowUpScheduledEmail email : followUpScheduledEmails) {
            Map<String, Object> emailMap = new HashMap<>();
            emailMap.put("id", email.getId());
            emailMap.put("recipient", email.getRecipient());
            emailMap.put("scheduledTime", email.getScheduledTime());
            emailMap.put("company", email.getCompany());
            emailMap.put("status", email.getStatus());
            emailMap.put("source", "Scheduled");
            emailMap.put("type", "FollowUp");
            combinedList.add(emailMap);
        }

        // Get all follow-up sent emails
        Iterable<FollowUpSentEmail> followUpSentEmails = followUpSentEmailRepository.findAll();
        for (FollowUpSentEmail email : followUpSentEmails) {
            Map<String, Object> emailMap = new HashMap<>();
            emailMap.put("id", email.getId());
            emailMap.put("recipient", email.getRecipient());
            emailMap.put("scheduledTime", email.getScheduledTime());
            emailMap.put("company", email.getCompany());
            emailMap.put("status", email.getStatus());
            emailMap.put("source", "Sent");
            emailMap.put("type", "FollowUp");
            combinedList.add(emailMap);
        }

        // Get all follow-up failed emails
        Iterable<FollowUpFailedEmail> followUpFailedEmails = followUpFailedEmailRepository.findAll();
        for (FollowUpFailedEmail email : followUpFailedEmails) {
            Map<String, Object> emailMap = new HashMap<>();
            emailMap.put("id", email.getId());
            emailMap.put("recipient", email.getRecipient());
            emailMap.put("scheduledTime", email.getScheduledTime());
            emailMap.put("company", email.getCompany());
            emailMap.put("status", email.getStatus());
            emailMap.put("source", "Failed");
            emailMap.put("type", "FollowUp");
            combinedList.add(emailMap);
        }

        // Sort by scheduled time (most recent first)
        combinedList.sort((a, b) -> {
            LocalDateTime timeA = (LocalDateTime) a.get("scheduledTime");
            LocalDateTime timeB = (LocalDateTime) b.get("scheduledTime");
            return timeB.compareTo(timeA);
        });

        return combinedList;
    }

    // Helper method to count total emails (for totalSent, totalFailed, totalScheduled)
    private long countTotalEmails(long total) {
        return total;  // If summary total is already correctly counted, no need to split further.
    }

    // Method to count sent emails (count individual recipients)
    private long countSentEmails(LocalDateTime start, LocalDateTime end) {
        long totalSent = 0;

        // Fetch all emails that were SENT between the given time range
        Iterable<SentEmails> emails = sentEmailsRepository.findByStatusAndScheduledTimeBetween("SENT", start, end);

        // Iterate through each email
        for (SentEmails email : emails) {
            String[] recipients = email.getRecipient().split(",");  // Split recipients by comma
            totalSent += recipients.length;  // Add the number of recipients in this email to the total count
        }

        // Also include follow-up sent emails
        Iterable<FollowUpSentEmail> followUpEmails = followUpSentEmailRepository.findByStatusAndScheduledTimeBetween("SENT", start, end);

        for (FollowUpSentEmail email : followUpEmails) {
            String[] recipients = email.getRecipient().split(",");  // Split recipients by comma
            totalSent += recipients.length;  // Add the number of recipients in this email to the total count
        }

        return totalSent;
    }

    // Method to count failed emails (count individual recipients)
    private long countFailedEmails(LocalDateTime start, LocalDateTime end) {
        long totalFailed = 0;

        // Fetch all emails that were FAILED between the given time range
        Iterable<FailedEmails> emails = failedEmailsRepository.findByStatusAndScheduledTimeBetween("FAILED", start, end);

        // Iterate through each email
        for (FailedEmails email : emails) {
            String[] recipients = email.getRecipient().split(",");  // Split recipients by comma
            totalFailed += recipients.length;  // Add the number of recipients in this email to the total count
        }

        // Also include follow-up failed emails
        Iterable<FollowUpFailedEmail> followUpEmails = followUpFailedEmailRepository.findByStatusAndScheduledTimeBetween("FAILED", start, end);

        for (FollowUpFailedEmail email : followUpEmails) {
            String[] recipients = email.getRecipient().split(",");  // Split recipients by comma
            totalFailed += recipients.length;  // Add the number of recipients in this email to the total count
        }

        return totalFailed;
    }

    // Method to count scheduled emails (count individual recipients)
    private long countScheduledEmails(LocalDateTime start, LocalDateTime end) {
        long totalScheduled = 0;

        // Fetch all emails that are PENDING (scheduled) between the given time range
        Iterable<ScheduledEmail> emails = scheduledEmailRepository.findByStatusAndScheduledTimeBetween("PENDING", start, end);

        // Iterate through each email
        for (ScheduledEmail email : emails) {
            String[] recipients = email.getRecipient().split(",");  // Split recipients by comma
            totalScheduled += recipients.length;  // Add the number of recipients in this email to the total count
        }

        // Also include follow-up scheduled emails
        Iterable<FollowUpScheduledEmail> followUpEmails = followUpScheduledEmailRepository.findByStatusAndScheduledTimeBetween("PENDING", start, end);

        for (FollowUpScheduledEmail email : followUpEmails) {
            String[] recipients = email.getRecipient().split(",");  // Split recipients by comma
            totalScheduled += recipients.length;  // Add the number of recipients in this email to the total count
        }

        return totalScheduled;
    }

    // 1. Send follow-up for Invitation with "Sent" status
    @PostMapping("/invitation/sent/{id}/follow-up")
    public ResponseEntity<ErrorResponse> sendFollowUpForInvitation(
            @PathVariable Long id,
            @RequestHeader("username") String username,
            @RequestBody Map<String, Object> request){
        try {
            // Check user authorization
            User user = userRepository.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("User not found."));
            }
            if (!user.isApproved()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse("User not approved. Please contact the admin."));
            }

            // Get the original sent email
            Optional<SentEmails> optionalSentEmail = sentEmailsRepository.findById(id);
            if (!optionalSentEmail.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Sent email not found."));
            }
            // Get the new scheduled time from the request
            String scheduledTimeStr = (String) request.get("scheduledTime");
            if (scheduledTimeStr == null) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Scheduled time is required"));
            }
            LocalDateTime scheduledTime = LocalDateTime.parse(scheduledTimeStr);

            SentEmails sentEmail = optionalSentEmail.get();
            FollowUpScheduledEmail followUpEmail = new FollowUpScheduledEmail();
            // Prepare follow-up email using data from the original email
            followUpEmail.setRecipient(sentEmail.getRecipient());
            followUpEmail.setCompany(sentEmail.getCompany());
            followUpEmail.setDesignation(sentEmail.getDesignation());
            followUpEmail.setPhone_Number(sentEmail.getPhone_Number());
            followUpEmail.setName(sentEmail.getName());
            followUpEmail.setSalutation(sentEmail.getSalutation());
            followUpEmail.setScheduledTime(scheduledTime);
            followUpEmail.setStatus("PENDING");
            followUpEmail.setYear(sentEmail.getYear());
            followUpEmail.setUsername(sentEmail.getUsername());

            currentNameService.updateUsername(username);

            // Schedule the follow-up email
            ResponseEntity<?> response = followUpEmailService.scheduleEmail(followUpEmail);

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(new ErrorResponse("Follow-up email scheduled successfully"));
            } else {
                return ResponseEntity.status(response.getStatusCode())
                        .body((ErrorResponse) response.getBody());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred: " + e.getMessage()));
        }
    }

    // 2.1 Delete failed invitation email
    @DeleteMapping("/invitation/failed/{id}")
    public ResponseEntity<ErrorResponse> deleteFailedInvitation(@PathVariable Long id) {
        try {
            Optional<FailedEmails> optionalFailedEmail = failedEmailsRepository.findById(id);
            if (!optionalFailedEmail.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Failed email not found."));
            }

            failedEmailsRepository.deleteById(id);
            return ResponseEntity.ok(new ErrorResponse("Failed email deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred: " + e.getMessage()));
        }
    }

    // 2.2 Reschedule failed invitation email
    @PostMapping("/invitation/failed/{id}/reschedule")
    public ResponseEntity<ErrorResponse> rescheduleFailedInvitation(
            @PathVariable Long id,
            @RequestHeader("username") String username,
            @RequestBody Map<String, Object> request) {
        try {
            // Check user authorization
            User user = userRepository.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("User not found."));
            }
            if (!user.isApproved()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse("User not approved. Please contact the admin."));
            }

            // Get the original failed email
            Optional<FailedEmails> optionalFailedEmail = failedEmailsRepository.findById(id);
            if (!optionalFailedEmail.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Failed email not found."));
            }

            FailedEmails failedEmail = optionalFailedEmail.get();

            // Get the new scheduled time from the request
            String scheduledTimeStr = (String) request.get("scheduledTime");
            if (scheduledTimeStr == null) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Scheduled time is required"));
            }

            LocalDateTime scheduledTime = LocalDateTime.parse(scheduledTimeStr);

            // Create a new scheduled email from the failed one
            ScheduledEmail newEmail = new ScheduledEmail();
            newEmail.setRecipient(failedEmail.getRecipient());
            newEmail.setCompany(failedEmail.getCompany());
            newEmail.setDesignation(failedEmail.getDesignation());
            newEmail.setName(failedEmail.getName());
            newEmail.setNirfYear(failedEmail.getnirfYear());
            newEmail.setPhone_Number(user.getPhone_Number());
            newEmail.setRank(failedEmail.getRank());
            newEmail.setSalutation(failedEmail.getSalutation());
            newEmail.setYear(failedEmail.getYear());
            newEmail.setUsername(failedEmail.getUsername());
            newEmail.setScheduledTime(scheduledTime);
            newEmail.setStatus("PENDING");

            currentNameService.updateUsername(username);

            // Schedule the email
            ResponseEntity<?> response = emailService.scheduleEmail(newEmail);

            if (response.getStatusCode().is2xxSuccessful()) {
                // Delete the failed email after rescheduling
                failedEmailsRepository.deleteById(id);
                return ResponseEntity.ok(new ErrorResponse("Email rescheduled successfully"));
            } else {
                return ResponseEntity.status(response.getStatusCode())
                        .body((ErrorResponse) response.getBody());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred: " + e.getMessage()));
        }
    }

    // 3. Delete scheduled invitation email
    @DeleteMapping("/invitation/scheduled/{id}")
    public ResponseEntity<ErrorResponse> deleteScheduledInvitation(@PathVariable Long id) {
        try {
            Optional<ScheduledEmail> optionalScheduledEmail = scheduledEmailRepository.findById(id);
            if (!optionalScheduledEmail.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Scheduled email not found."));
            }

            scheduledEmailRepository.deleteById(id);
            return ResponseEntity.ok(new ErrorResponse("Scheduled email deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred: " + e.getMessage()));
        }
    }

    // 4. Send follow-up for FollowUp with "Sent" status
    @PostMapping("/followup/sent/{id}/resend")
    public ResponseEntity<ErrorResponse> resendFollowUp(
            @PathVariable Long id,
            @RequestHeader("username") String username,
            @RequestBody Map<String, Object> request) {
        try {
            // Check user authorization
            User user = userRepository.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("User not found."));
            }
            if (!user.isApproved()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse("User not approved. Please contact the admin."));
            }

            // Get the original sent follow-up email
            Optional<FollowUpSentEmail> optionalSentEmail = followUpSentEmailRepository.findById(id);
            if (!optionalSentEmail.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Sent follow-up email not found."));
            }
            // Get the new scheduled time from the request
            String scheduledTimeStr = (String) request.get("scheduledTime");
            if (scheduledTimeStr == null) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Scheduled time is required"));
            }

            LocalDateTime scheduledTime = LocalDateTime.parse(scheduledTimeStr);

            FollowUpScheduledEmail followUpEmail = new FollowUpScheduledEmail();

            FollowUpSentEmail sentEmail = optionalSentEmail.get();

            // Prepare follow-up email using data from the original email
            followUpEmail.setRecipient(sentEmail.getRecipient());
            followUpEmail.setCompany(sentEmail.getCompany());
            followUpEmail.setDesignation(sentEmail.getDesignation());
            followUpEmail.setPhone_Number(sentEmail.getPhone_Number());
            followUpEmail.setName(sentEmail.getName());
            followUpEmail.setSalutation(sentEmail.getSalutation());
            followUpEmail.setScheduledTime(scheduledTime);
            followUpEmail.setStatus("PENDING");
            followUpEmail.setYear(sentEmail.getYear());
            followUpEmail.setUsername(sentEmail.getUsername());

            currentNameService.updateUsername(username);

            // Schedule the follow-up email
            ResponseEntity<?> response = followUpEmailService.scheduleEmail(followUpEmail);

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(new ErrorResponse("Follow-up email scheduled successfully"));
            } else {
                return ResponseEntity.status(response.getStatusCode())
                        .body((ErrorResponse) response.getBody());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred: " + e.getMessage()));
        }
    }

    // 5.1 Delete failed follow-up email
    @DeleteMapping("/followup/failed/{id}")
    public ResponseEntity<ErrorResponse> deleteFailedFollowUp(@PathVariable Long id) {
        try {
            Optional<FollowUpFailedEmail> optionalFailedEmail = followUpFailedEmailRepository.findById(id);
            if (!optionalFailedEmail.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Failed follow-up email not found."));
            }

            followUpFailedEmailRepository.deleteById(id);
            return ResponseEntity.ok(new ErrorResponse("Failed follow-up email deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred: " + e.getMessage()));
        }
    }

    // 5.2 Reschedule failed follow-up email
    @PostMapping("/followup/failed/{id}/reschedule")
    public ResponseEntity<ErrorResponse> rescheduleFailedFollowUp(
            @PathVariable Long id,
            @RequestHeader("username") String username,
            @RequestBody Map<String, Object> request) {
        try {
            // Check user authorization
            User user = userRepository.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("User not found."));
            }
            if (!user.isApproved()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse("User not approved. Please contact the admin."));
            }

            // Get the original failed email
            Optional<FollowUpFailedEmail> optionalFailedEmail = followUpFailedEmailRepository.findById(id);
            if (!optionalFailedEmail.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Failed follow-up email not found."));
            }

            FollowUpFailedEmail failedEmail = optionalFailedEmail.get();

            // Get the new scheduled time from the request
            String scheduledTimeStr = (String) request.get("scheduledTime");
            if (scheduledTimeStr == null) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Scheduled time is required"));
            }

            LocalDateTime scheduledTime = LocalDateTime.parse(scheduledTimeStr);

            // Create a new scheduled follow-up email from the failed one
            FollowUpScheduledEmail newEmail = new FollowUpScheduledEmail();
            newEmail.setRecipient(failedEmail.getRecipient());
            newEmail.setCompany(failedEmail.getCompany());
            newEmail.setDesignation(failedEmail.getDesignation());
            newEmail.setPhone_Number(failedEmail.getPhone_Number());
            newEmail.setName(failedEmail.getName());
            newEmail.setSalutation(failedEmail.getSalutation());
            newEmail.setScheduledTime(scheduledTime);
            newEmail.setStatus("PENDING");
            newEmail.setYear(failedEmail.getYear());
            newEmail.setUsername(failedEmail.getUsername());

            currentNameService.updateUsername(username);

            // Schedule the follow-up email
            ResponseEntity<?> response = followUpEmailService.scheduleEmail(newEmail);

            if (response.getStatusCode().is2xxSuccessful()) {
                // Delete the failed email after rescheduling
                followUpFailedEmailRepository.deleteById(id);
                return ResponseEntity.ok(new ErrorResponse("Follow-up email rescheduled successfully"));
            } else {
                return ResponseEntity.status(response.getStatusCode())
                        .body((ErrorResponse) response.getBody());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred: " + e.getMessage()));
        }
    }

    // 6. Delete scheduled follow-up email
    @DeleteMapping("/followup/scheduled/{id}")
    public ResponseEntity<ErrorResponse> deleteScheduledFollowUp(@PathVariable Long id) {
        try {
            Optional<FollowUpScheduledEmail> optionalScheduledEmail = followUpScheduledEmailRepository.findById(id);
            if (!optionalScheduledEmail.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Scheduled follow-up email not found."));
            }

            followUpScheduledEmailRepository.deleteById(id);
            return ResponseEntity.ok(new ErrorResponse("Scheduled follow-up email deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred: " + e.getMessage()));
        }
    }
}