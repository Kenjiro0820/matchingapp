package com.example.matchingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MatchResponse {
    private Long matchId;
    private Long otherUserId;
    private String partnerName;
    private String partnerImageUrl;
    private String groupTitle;
    private String area;
    private String status;
    private LocalDateTime matchedAt;
}
