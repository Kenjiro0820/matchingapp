package com.example.matchingapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "representative_profiles")
@Getter
@Setter
@NoArgsConstructor
public class RepresentativeProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String nickname;

    @Column(length = 1000)
    private String profileImageUrl;

    @Column(length = 3000)
    private String bio;

    private String ageRange;
    private String occupation;
    private String drinkingLevel;

    @Column(length = 1000)
    private String personalityTags;

    private String preferredArea;
    private Boolean isActive = true;
}
