package com.spring.JournalApplication.controller;

import com.spring.JournalApplication.entity.JournalEntry;
import com.spring.JournalApplication.entity.User;
import com.spring.JournalApplication.service.JournalEntryService;
import com.spring.JournalApplication.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getEntryById(@PathVariable ObjectId id) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User user = userService.findByUsername(username);
            List<JournalEntry> entries = user.getJournal_entries().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
            if(entries.isEmpty()) {
                log.info("Journal entry with id {} not found.", id);
                return new ResponseEntity<>("Entry not found", HttpStatus.NOT_FOUND);
            }
            log.info("Journal entry with id {} found.", id);
            JournalEntry entry = journalEntryService.getEntryById(id);
            return new ResponseEntity<>(entry, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving journal entry with id {}.", id);
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllJournalByUser() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User user = userService.findByUsername(username);
            if(user == null) {
                log.info("Journal entry with username {} not found.", username);
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
            log.info("Journal entry with username {} found.", username);
            return new ResponseEntity<>(user.getJournal_entries(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving journal entries for user.");
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry entry) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            entry.setAuthor(username);
            entry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(entry, username);
            log.info("Journal entry with id {} created.", entry.getId());
            return new ResponseEntity<>(entry, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating journal entry.");
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteEntryById(@PathVariable ObjectId id) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            if(journalEntryService.getEntryById(id) == null) {
                log.info("Journal entry with id {} not found.", id);
                return new ResponseEntity<>("Entry not found", HttpStatus.NOT_FOUND);
            }
            journalEntryService.deleteEntryById(id, username);
            log.info("Journal entry with id {} deleted.", id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error deleting journal entry with id {}.", id);
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<?> updateEntryById(@PathVariable ObjectId id, @RequestBody JournalEntry newEntry) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User user = userService.findByUsername(username);
            boolean ownsEntry = user.getJournal_entries().stream()
                    .anyMatch(x -> x.getId().equals(id));
            if (!ownsEntry) {
                log.info("Journal entry with id {} not found.", id);
                return new ResponseEntity<>("Entry not found in your account", HttpStatus.NOT_FOUND);
            }

            JournalEntry curr = journalEntryService.getEntryById(id);
            if (curr == null) {
                log.info("Journal entry with id {} not found.", id);
                return new ResponseEntity<>("Entry no longer exists", HttpStatus.NOT_FOUND);
            }

            if (newEntry.getTitle() != null && !newEntry.getTitle().isEmpty()) {
                curr.setTitle(newEntry.getTitle());
            }
            if (newEntry.getContent() != null && !newEntry.getContent().isEmpty()) {
                curr.setContent(newEntry.getContent());
            }

            log.info("Journal entry with id {} updated.", id);
            journalEntryService.saveEntry(curr);
            return new ResponseEntity<>(curr, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error updating journal entry with id {}.", id);
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
