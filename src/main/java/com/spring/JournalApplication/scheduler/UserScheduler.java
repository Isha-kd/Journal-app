package com.spring.JournalApplication.scheduler;

import com.spring.JournalApplication.entity.User;
import com.spring.JournalApplication.repository.UserRepositoryImpl;
import com.spring.JournalApplication.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Scheduled(cron = "0 0 9 * * SUN") // Every Sunday at 9 AM
    public void fetchUsersAndSendMail() {
        List<User> users = userRepository.getUsersForNotification();
        for (User user : users) {
            String to = user.getEmail();
            String subject = "Weekly Notification from Journal Application";
            String body = "Dear " + user.getUsername() + ",\n\nIt's been a while. Take a break and share your latest thoughts.\n\nBest regards,\nJournal Team";
            emailService.sendEmail(to, subject, body);
            log.info("Email sent to: {} at {}", to, System.currentTimeMillis());
        }
    }
}
