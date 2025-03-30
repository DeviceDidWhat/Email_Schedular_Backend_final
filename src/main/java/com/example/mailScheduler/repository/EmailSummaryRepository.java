package com.example.mailScheduler.repository;

import com.example.mailScheduler.model.EmailSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailSummaryRepository extends JpaRepository<EmailSummary, Long> {
    Optional<EmailSummary> findFirstByOrderById();
}
