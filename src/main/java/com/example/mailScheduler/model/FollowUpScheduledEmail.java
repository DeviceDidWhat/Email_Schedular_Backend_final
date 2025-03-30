package com.example.mailScheduler.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "followup_scheduled_emails")
public class FollowUpScheduledEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipient;

    @Column(name = "scheduled_time")
    private LocalDateTime scheduledTime;

    @Column(name = "Company")
    private String Company;

    @Column(name = "Salutation")
    private String Salutation;

    @Column(name = "Name")
    private String Name;

    @Column(name = "Designation")
    private String Designation;

    @Column(name = "Phone_Number")
    private Long Phone_Number;

    @Column(name = "year")
    private String year;

    @Column(name = "user")
    private String Username;

    @Column(name = "error_message", length = 500)
    private String errorMessage; // Store the error details

    private String status; // PENDING, SENT, FAILED

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


    public Long getPhone_Number() {
        return Phone_Number;
    }

    public void setPhone_Number(Long phone_Number) {
        Phone_Number = phone_Number;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSalutation() {
        return Salutation;
    }

    public void setSalutation(String salutation) {
        Salutation = salutation;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
