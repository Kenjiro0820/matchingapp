package com.example.matchingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SwipeResultResponse {
    private boolean matched;
    private Long matchId;
    private String message;
}