package com.example.matchingapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "group_posts")
@Getter
@Setter
@NoArgsConstructor
public class GroupPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organizer_user_id", nullable = false)
    private Long organizerUserId;

    @Column(nullable = false)
    private String title;

    @Column(length = 3000)
    private String description;

    private String area;

    @Column(nullable = false)
    private String status = "OPEN";

    private LocalDateTime scheduledAt;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null || status.isBlank()) {
            status = "OPEN";
        }
    }
}