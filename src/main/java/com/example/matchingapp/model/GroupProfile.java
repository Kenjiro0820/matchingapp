package com.example.matchingapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "group_profiles")
@Getter
@Setter
public class GroupProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_user_id", nullable = false)
    private Long ownerUserId;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "group_image_url", length = 500)
    private String groupImageUrl;

    @Column(name = "introduction", length = 1000)
    private String introduction;

    @Column(name = "area", length = 100)
    private String area;

    @Column(name = "preferred_area", length = 100)
    private String preferredArea;

    @Column(name = "male_count", nullable = false)
    private Integer maleCount = 0;

    @Column(name = "female_count", nullable = false)
    private Integer femaleCount = 0;

    @Column(name = "age_min")
    private Integer ageMin;

    @Column(name = "age_max")
    private Integer ageMax;

    @Column(name = "preferred_age_min")
    private Integer preferredAgeMin;

    @Column(name = "preferred_age_max")
    private Integer preferredAgeMax;

    @Column(name = "budget_per_person")
    private Integer budgetPerPerson;

    @Column(name = "meeting_style", length = 50)
    private String meetingStyle;

    @Column(name = "available_days", length = 255)
    private String availableDays;

    @Column(name = "preferred_group_description", length = 500)
    private String preferredGroupDescription;

    @Column(name = "status", nullable = false, length = 30)
    private String status = "ACTIVE";

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (this.maleCount == null) {
            this.maleCount = 0;
        }
        if (this.femaleCount == null) {
            this.femaleCount = 0;
        }
        if (this.status == null) {
            this.status = "ACTIVE";
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}