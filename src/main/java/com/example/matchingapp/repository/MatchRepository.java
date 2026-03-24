package com.example.matchingapp.repository;

import com.example.matchingapp.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByUserAIdOrUserBIdOrderByMatchedAtDesc(Long userAId, Long userBId);

    boolean existsByUserAIdAndUserBIdAndGroupProfileAIdAndGroupProfileBId(
            Long userAId,
            Long userBId,
            Long groupProfileAId,
            Long groupProfileBId
    );

    boolean existsByUserAIdAndUserBIdAndGroupProfileAIdAndGroupProfileBIdOrUserAIdAndUserBIdAndGroupProfileAIdAndGroupProfileBId(
            Long userAId1,
            Long userBId1,
            Long groupProfileAId1,
            Long groupProfileBId1,
            Long userAId2,
            Long userBId2,
            Long groupProfileAId2,
            Long groupProfileBId2
    );
}