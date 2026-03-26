package com.example.matchingapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userAId;
    private Long userBId;
    private Long groupProfileAId;
    private Long groupProfileBId;
    private String status;
    private LocalDateTime matchedAt;

    @PrePersist
    public void onCreate() {
        if (matchedAt == null) {
            matchedAt = LocalDateTime.now();
        }
    }
}
