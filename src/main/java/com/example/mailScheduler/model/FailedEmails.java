package com.example.mailScheduler.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "failed_emails")
public class FailedEmails {
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

    @Column(name = "NIRF_rank")
    private Long Rank;

    @Column(name = "NIRF_Year")
    private Long nirfYear;

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

    // Getters and Setters


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

    public Long getPhone_Number() {
        return Phone_Number;
    }

    public void setPhone_Number(Long phone_Number) {
        Phone_Number = phone_Number;
    }

    public Long getnirfYear() {
        return nirfYear;
    }

    public void setnirfYear(Long nirfYear) {
        this.nirfYear = nirfYear;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Long getRank() {
        return Rank;
    }

    public void setRank(Long rank) {
        Rank = rank;
    }

    public String getSalutation() {
        return Salutation;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setSalutation(String salutation) {
        Salutation = salutation;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
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

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        this.Company = company;
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


}