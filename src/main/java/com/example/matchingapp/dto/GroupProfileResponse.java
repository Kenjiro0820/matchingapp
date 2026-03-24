package com.example.matchingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GroupProfileResponse {
    private Long id;
    private Long ownerUserId;
    private String title;
    private String groupImageUrl;
    private String introduction;
    private String area;
    private String preferredArea;
    private Integer maleCount;
    private Integer femaleCount;
    private Integer ageMin;
    private Integer ageMax;
    private Integer preferredAgeMin;
    private Integer preferredAgeMax;
    private Integer budgetPerPerson;
    private String meetingStyle;
    private String availableDays;
    private String preferredGroupDescription;
    private String status;
}