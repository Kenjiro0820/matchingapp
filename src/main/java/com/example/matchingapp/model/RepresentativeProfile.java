package com.example.matchingapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "representative_profiles")
@Getter
@Setter
public class RepresentativeProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    @Column(name = "bio", length = 500)
    private String bio;

    @Column(name = "age_range", length = 30)
    private String ageRange;

    @Column(name = "occupation", length = 100)
    private String occupation;

    @Column(name = "drinking_level", length = 30)
    private String drinkingLevel;

    @Column(name = "personality_tags", length = 255)
    private String personalityTags;

    @Column(name = "preferred_area", length = 100)
    private String preferredArea;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.isActive == null) {
            this.isActive = true;
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}