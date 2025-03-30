package com.example.mailScheduler.repository;

import com.example.mailScheduler.model.FailedEmails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FailedEmailsRepository extends JpaRepository<FailedEmails, Long> {

    // Find emails by status and scheduled time before the provided time
    List<FailedEmails> findByStatusAndScheduledTimeBefore(String status, LocalDateTime scheduledTime);

    // Find emails by status
    List<FailedEmails> findByStatus(String status);

    // Count emails by status
    long countByStatus(String status);

    // Count emails sent successfully (with SENT status) between a specific time range (e.g., today or yesterday)
    long countByStatusAndScheduledTimeBetween(String status, LocalDateTime start, LocalDateTime end);

    // Count emails scheduled before a specific time (for statistics)
    long countByStatusAndScheduledTimeBefore(String status, LocalDateTime scheduledTime);

    // Find emails scheduled after a specific time (for statistics)
    List<FailedEmails> findByScheduledTimeAfter(LocalDateTime scheduledTime);

//    failedEmails[] findByStatusAndScheduledTimeBetween(String status, LocalDateTime scheduledTimeAfter, LocalDateTime scheduledTimeBefore);

    Iterable<FailedEmails> findByStatusAndScheduledTimeBetween(String status, LocalDateTime scheduledTimeAfter, LocalDateTime scheduledTimeBefore);

    Iterable<Object> findByScheduledTimeAfterOrderByScheduledTimeDesc(LocalDateTime scheduledTimeAfter);
}