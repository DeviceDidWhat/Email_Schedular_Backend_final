package com.example.mailScheduler.repository;

import com.example.mailScheduler.model.CurrentName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentNameRepository extends JpaRepository<CurrentName, Integer> {
    @Query("SELECT c.username FROM CurrentName c WHERE c.id = :id")
    String findUsernameById(@Param("id") Integer id);

}
