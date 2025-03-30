package com.example.mailScheduler.service;
import com.example.mailScheduler.model.CurrentName;
import com.example.mailScheduler.repository.CurrentNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrentNameService {

    @Autowired
    private CurrentNameRepository currentNameRepository;

    public void updateUsername(String username) {
        // Check if the table is empty
        if (currentNameRepository.count() == 0) {
            // Insert a new row if the table is empty
            CurrentName curretName = new CurrentName();
            curretName.setId(1); // Fixed ID for single-row table
            curretName.setUsername(username);
            currentNameRepository.save(curretName);
        } else {
            // Update the existing row
            CurrentName userState = currentNameRepository.findById(1)
                    .orElseThrow(() -> new IllegalStateException("UserState row not found"));
            userState.setUsername(username);
            currentNameRepository.save(userState);
        }
    }
}
