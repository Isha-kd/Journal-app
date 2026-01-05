package com.spring.JournalApplication.controller;

import com.spring.JournalApplication.entity.User;
import com.spring.JournalApplication.repository.UserRepository;
import com.spring.JournalApplication.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User userInDb = userService.findByUsername(username);
        if(userInDb != null) {
            userInDb.setUsername(user.getUsername());
            userInDb.setPassword(user.getPassword());
            userService.saveUser(userInDb, true);
            log.info("User {} updated to {}.", username, user.getUsername());
            return new ResponseEntity<>("User updated successfully", null, 200);
        }
        log.info("User {} not found.", username);
        return new ResponseEntity<>("User not found", null, 404);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        userRepository.deleteByUsername(username);
        log.info("User {} deleted.", username);
        return new ResponseEntity<>("User deleted!", HttpStatus.OK);
    }
}
