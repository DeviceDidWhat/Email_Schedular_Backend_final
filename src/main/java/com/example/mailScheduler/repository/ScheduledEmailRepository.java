package com.example.mailScheduler.repository;

import com.example.mailScheduler.model.ScheduledEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledEmailRepository extends JpaRepository<ScheduledEmail, Long> {

    // Find emails by status and scheduled time before the provided time
    List<ScheduledEmail> findByStatusAndScheduledTimeBefore(String status, LocalDateTime scheduledTime);

    // Find emails by status
    List<ScheduledEmail> findByStatus(String status);

    // Count emails by status
    long countByStatus(String status);

    // Count emails sent successfully (with SENT status) between a specific time range (e.g., today or yesterday)
    long countByStatusAndScheduledTimeBetween(String status, LocalDateTime start, LocalDateTime end);

    // Count emails scheduled before a specific time (for statistics)
    long countByStatusAndScheduledTimeBefore(String status, LocalDateTime scheduledTime);

    // Find emails scheduled after a specific time (for statistics)
    List<ScheduledEmail> findByScheduledTimeAfter(LocalDateTime scheduledTime);

//    ScheduledEmail[] findByStatusAndScheduledTimeBetween(String status, LocalDateTime scheduledTimeAfter, LocalDateTime scheduledTimeBefore);

    Iterable<ScheduledEmail> findByStatusAndScheduledTimeBetween(String status, LocalDateTime scheduledTimeAfter, LocalDateTime scheduledTimeBefore);

    Iterable<Object> findByStatusAndScheduledTimeAfterOrderByScheduledTimeDesc(String status, LocalDateTime scheduledTimeAfter);
}