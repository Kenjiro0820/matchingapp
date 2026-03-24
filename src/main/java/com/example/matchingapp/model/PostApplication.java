package com.example.matchingapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_applications")
@Getter
@Setter
public class PostApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long postId;
    private Long applicantUserId;
    private String message;

    private String status;

    private LocalDateTime createdAt;
}