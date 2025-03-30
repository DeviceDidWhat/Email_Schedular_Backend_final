package com.example.mailScheduler.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_summary")
public class EmailSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_sent")
    private Long totalSent;

    @Column(name = "total_failed")
    private Long totalFailed;

    @Column(name = "total_scheduled")
    private Long totalScheduled;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    // Getters and Setters
    public Long getTotalSent() {
        return totalSent;
    }

    public void setTotalSent(Long totalSent) {
        this.totalSent = totalSent;
    }

    public Long getTotalFailed() {
        return totalFailed;
    }

    public void setTotalFailed(Long totalFailed) {
        this.totalFailed = totalFailed;
    }

    public Long getTotalScheduled() {
        return totalScheduled;
    }

    public void setTotalScheduled(Long totalScheduled) {
        this.totalScheduled = totalScheduled;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}