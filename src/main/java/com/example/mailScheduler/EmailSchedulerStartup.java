package com.example.mailScheduler;

import com.example.mailScheduler.service.EmailService;
import com.example.mailScheduler.service.FollowUpEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EmailSchedulerStartup {

    @Autowired
    private EmailService emailService;

    @Autowired
    private FollowUpEmailService followUpEmailService;

    @EventListener(ApplicationReadyEvent.class)
    public void processPendingEmails()
    {
        System.out.println("Processing pending emails at startup...");
        emailService.processMissedEmails();

        // Update the email summary
        emailService.updateEmailSummary();

        System.out.println("Processing pending follow up emails at startup...");
        followUpEmailService.processMissedEmails();

        // Update the email summary
//        followUpEmailService.updateEmailSummary();
    }
}