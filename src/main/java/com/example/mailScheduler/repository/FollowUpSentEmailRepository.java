package com.example.mailScheduler.repository;

import com.example.mailScheduler.model.FollowUpSentEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface FollowUpSentEmailRepository extends JpaRepository<FollowUpSentEmail, Long> {
    Iterable<FollowUpSentEmail> findByStatusAndScheduledTimeBetween(String status, LocalDateTime scheduledTimeAfter, LocalDateTime scheduledTimeBefore);
}

