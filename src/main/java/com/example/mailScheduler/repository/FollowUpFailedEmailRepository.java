package com.example.mailScheduler.repository;

import com.example.mailScheduler.model.FollowUpFailedEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface FollowUpFailedEmailRepository extends JpaRepository<FollowUpFailedEmail, Long> {
    Iterable<FollowUpFailedEmail> findByStatusAndScheduledTimeBetween(String status, LocalDateTime scheduledTimeAfter, LocalDateTime scheduledTimeBefore);
}
