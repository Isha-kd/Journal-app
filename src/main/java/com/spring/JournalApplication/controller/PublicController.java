package com.spring.JournalApplication.controller;

import com.spring.JournalApplication.entity.User;
import com.spring.JournalApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @GetMapping("/health-check")
    public String check() {
        return "OK";
    }

    @PostMapping("/create-user")
    public boolean createUser(@RequestBody User user) {
        userService.saveUser(user, true);
        return true;
    }
}
