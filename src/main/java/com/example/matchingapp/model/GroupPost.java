package com.example.matchingapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "group_posts")
@Getter
@Setter
public class GroupPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long organizerUserId;
    private String title;
    private String description;
    private String area;
    private LocalDateTime scheduledAt;

    private String status;
}