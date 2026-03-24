package com.example.matchingapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Getter
@Setter
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_a_id", nullable = false)
    private Long userAId;

    @Column(name = "user_b_id", nullable = false)
    private Long userBId;

    @Column(name = "group_profile_a_id", nullable = false)
    private Long groupProfileAId;

    @Column(name = "group_profile_b_id", nullable = false)
    private Long groupProfileBId;

    @Column(name = "status", nullable = false, length = 30)
    private String status = "MATCHED";

    @Column(name = "matched_at", nullable = false)
    private LocalDateTime matchedAt;

    @Column(name = "last_message_at")
    private LocalDateTime lastMessageAt;

    @PrePersist
    public void onCreate() {
        if (this.status == null) {
            this.status = "MATCHED";
        }
        if (this.matchedAt == null) {
            this.matchedAt = LocalDateTime.now();
        }
    }
}