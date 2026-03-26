package com.example.matchingapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "swipe_actions")
@Getter
@Setter
@NoArgsConstructor
public class SwipeAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long fromUserId;
    private Long toUserId;
    private Long fromGroupProfileId;
    private Long toGroupProfileId;
    private String action;
}
