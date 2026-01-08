package com.spring.JournalApplication.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.spring.JournalApplication.dtos.JournalEntryDTO;
import com.spring.JournalApplication.entity.JournalEntry;
import com.spring.JournalApplication.entity.User;
import com.spring.JournalApplication.repository.JournalEntryRepository;
import com.spring.JournalApplication.service.CachingService;
import com.spring.JournalApplication.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/health-check")
    public String check() {
        return "OK";
    }

    @PostMapping("/create-user")
    public boolean createUser(@RequestBody User user) {
        userService.saveUser(user, true);
        log.info("User {} created.", user);
        return true;
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
