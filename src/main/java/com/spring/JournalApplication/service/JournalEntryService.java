package com.spring.JournalApplication.service;

import com.spring.JournalApplication.entity.JournalEntry;
import com.spring.JournalApplication.entity.User;
import com.spring.JournalApplication.repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry entry, String username) {
        try {
            User user = userService.findByUsername(username);
            JournalEntry je = journalEntryRepository.save(entry);
            user.getJournal_entries().add(je);
            userService.saveUser(user);
        } catch (Exception e) {
            log.error("Error saving journal entry for user {}: {}", username, e.getMessage());
            throw new RuntimeException("Something went wrong while saving the journal entry");
        }
    }

    public void saveEntry(JournalEntry entry) {
        journalEntryRepository.save(entry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public JournalEntry getEntryById(ObjectId id) {
        return journalEntryRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteEntryById(ObjectId id, String username) {
        try {
            User user = userService.findByUsername(username);
            boolean b = user.getJournal_entries().removeIf(entry -> entry.getId().equals(id));
            if(b) {
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
                log.info("Journal entry with id {} was deleted", id);
            } else {
                log.warn("Journal entry with id {} not found for user {}", id, username);
            }
        } catch (Exception e) {
            log.error("Error deleting journal entry with id {}: {}", id, e.getMessage());
            throw new RuntimeException("An error occurred while deleting the journal entry");
        }
    }

}
