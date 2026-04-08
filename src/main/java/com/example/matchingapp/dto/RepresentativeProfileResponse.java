package com.example.matchingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RepresentativeProfileResponse {
    private Long id;
    private Long userId;
    private String nickname;
    private String profileImageUrl;
    private String bio;
    private String ageRange;
    private String occupation;
    private String drinkingLevel;
    private String personalityTags;
    private String preferredArea;
    private Boolean isActive;
}
