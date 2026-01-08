package com.spring.JournalApplication.repository;

import com.spring.JournalApplication.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JournalEntryRepository extends MongoRepository<JournalEntry, ObjectId> {
    List<JournalEntry> findTop10ByOrderByDateDesc();
}
