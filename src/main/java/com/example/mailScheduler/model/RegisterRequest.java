package com.example.mailScheduler.model;

import jakarta.persistence.Column;

public class RegisterRequest {
    private String username;
    private String password;
    private String Name;
    private String Designation;
    private Long Phone_Number;

    // Getters and setters omitted for brevity


    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

