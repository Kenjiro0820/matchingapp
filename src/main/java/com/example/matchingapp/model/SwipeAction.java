package com.example.matchingapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "swipe_actions",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_swipe_actions_unique",
            columnNames = {
                "from_user_id",
                "to_user_id",
                "from_group_profile_id",
                "to_group_profile_id"
            }
        )
    }
)
@Getter
@Setter
public class SwipeAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_user_id", nullable = false)
    private Long fromUserId;

    @Column(name = "to_user_id", nullable = false)
    private Long toUserId;

    @Column(name = "from_group_profile_id", nullable = false)
    private Long fromGroupProfileId;

    @Column(name = "to_group_profile_id", nullable = false)
    private Long toGroupProfileId;

    @Column(name = "action", nullable = false, length = 10)
    private String action;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}