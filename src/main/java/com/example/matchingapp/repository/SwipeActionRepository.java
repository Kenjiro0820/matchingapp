package com.example.matchingapp.repository;

import com.example.matchingapp.model.SwipeAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SwipeActionRepository extends JpaRepository<SwipeAction, Long> {
    boolean existsByFromUserIdAndToUserIdAndFromGroupProfileIdAndToGroupProfileId(
            Long fromUserId,
            Long toUserId,
            Long fromGroupProfileId,
            Long toGroupProfileId
    );

    Optional<SwipeAction> findByFromUserIdAndToUserIdAndFromGroupProfileIdAndToGroupProfileId(
            Long fromUserId,
            Long toUserId,
            Long fromGroupProfileId,
            Long toGroupProfileId
    );
}
