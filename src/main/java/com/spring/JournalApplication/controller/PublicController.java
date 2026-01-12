package com.spring.JournalApplication.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.spring.JournalApplication.dtos.JournalEntryDTO;
import com.spring.JournalApplication.entity.JournalEntry;
import com.spring.JournalApplication.entity.User;
import com.spring.JournalApplication.repository.JournalEntryRepository;
import com.spring.JournalApplication.service.CachingService;
import com.spring.JournalApplication.service.UserDetailsServiceImpl;
import com.spring.JournalApplication.service.UserService;
import com.spring.JournalApplication.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private CachingService cachingService;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("/health-check")
    public String check() {
        return "OK";
    }

    @PostMapping("/signup")
    public boolean signup(@RequestBody User user) {
        userService.saveUser(user, true);
        log.info("User {} created.", user);
        return true;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Login failed for user {}.", user.getUsername());
            return new ResponseEntity<>("Incorrect credentials.", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/getRecentPosts")
    public List<JournalEntryDTO> getRecentPosts() {

        List<JournalEntryDTO> cached = cachingService.getFromCache("recentPosts", new TypeReference<List<JournalEntryDTO>>() {});
        if(cached != null && !cached.isEmpty()) {
            log.info("Cache found.");
            return cached;
        }

        List<JournalEntry> list = journalEntryRepository.findTop10ByOrderByDateDesc();
        List<JournalEntryDTO> dtoList = list.stream()
                .map(entity -> new JournalEntryDTO(
                        entity.getId() != null ? entity.getId().toHexString() : null,
                        entity.getAuthor(),
                        entity.getTitle(),
                        entity.getContent(),
                        entity.getDate() != null ? entity.getDate().toString() : null
                ))
                .toList();

        cachingService.putToCache("recentPosts", dtoList, Duration.ofMinutes(10));
        log.info("Cache updated.");
        return dtoList;
    }
}
