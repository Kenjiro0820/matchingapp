package com.example.matchingapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RepresentativeProfileRequest {
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