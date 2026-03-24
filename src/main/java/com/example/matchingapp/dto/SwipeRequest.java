package com.example.matchingapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SwipeRequest {
    private Long targetUserId;
    private Long targetGroupProfileId;
    private String action;
}