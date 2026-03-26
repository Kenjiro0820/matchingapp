package com.example.matchingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SwipeCandidateResponse {
    private Long targetUserId;
    private Long targetGroupProfileId;
    private String nickname;
    private String imageUrl;
    private String bio;
    private String ageRange;
    private String occupation;
    private String personalityTags;
    private String preferredArea;
    private String groupTitle;
    private String groupImageUrl;
    private String introduction;
    private String area;
    private Integer maleCount;
    private Integer femaleCount;
    private Integer ageMin;
    private Integer ageMax;
    private Integer budgetPerPerson;
    private String meetingStyle;
    private String availableDays;
    private String preferredGroupDescription;
}
