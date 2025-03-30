package com.example.mailScheduler.repository;

import com.example.mailScheduler.model.FollowUpScheduledEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface FollowUpScheduledEmailRepository extends JpaRepository<FollowUpScheduledEmail, Long> {
    Iterable<FollowUpScheduledEmail> findByStatusAndScheduledTimeBetween(String status, LocalDateTime scheduledTimeAfter, LocalDateTime scheduledTimeBefore);
}
