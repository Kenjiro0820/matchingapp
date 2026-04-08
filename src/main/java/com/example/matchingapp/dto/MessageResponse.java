package com.example.matchingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MessageResponse {
    private Long id;
    private Long senderUserId;
    private String body;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
