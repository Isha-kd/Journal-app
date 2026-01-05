package com.spring.JournalApplication.controller;

import com.spring.JournalApplication.entity.User;
import com.spring.JournalApplication.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/get-all-users")
    public ResponseEntity<?> getAllUsers() {
        log.trace("Request to get all users.");
        List<User> users = userService.getAll();
        if(users != null && !users.isEmpty()) {
            log.info("Successfully retrieved all journal entries");
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        log.info("No journal entries found");
        return new ResponseEntity<>("No users found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestBody User user) {
        log.trace("New Admin creation request.");
        userService.saveAdmin(user, true);
        return new ResponseEntity<>("Admin created successfully", HttpStatus.CREATED);
    }
}
