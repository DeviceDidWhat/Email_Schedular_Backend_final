package com.example.mailScheduler.repository;

import com.example.mailScheduler.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    List<User> findByIsApprovedFalse(); // Custom query to find unapproved users

}

