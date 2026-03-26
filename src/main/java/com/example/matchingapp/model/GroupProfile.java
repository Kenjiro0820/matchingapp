package com.example.matchingapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "group_profiles")
@Getter
@Setter
@NoArgsConstructor
public class GroupProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ownerUserId;
    private String title;

    @Column(length = 1000)
    private String groupImageUrl;

    @Column(length = 3000)
    private String introduction;

    private String area;
    private String preferredArea;
    private Integer maleCount = 0;
    private Integer femaleCount = 0;
    private Integer ageMin;
    private Integer ageMax;
    private Integer preferredAgeMin;
    private Integer preferredAgeMax;
    private Integer budgetPerPerson;
    private String meetingStyle;

    @Column(length = 1000)
    private String availableDays;

    @Column(length = 3000)
    private String preferredGroupDescription;

    private String status = "ACTIVE";
}
