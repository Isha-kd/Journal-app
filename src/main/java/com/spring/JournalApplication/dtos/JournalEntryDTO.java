package com.spring.JournalApplication.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntryDTO {
    private String id;
    private String author;
    private String title;
    private String content;
    private String date;
}
