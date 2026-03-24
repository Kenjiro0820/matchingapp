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

    boolean existsByUserAIdAndUserBId(Long userAId, Long userBId);

    boolean existsByUserBIdAndUserAId(Long userBId, Long userAId);
}