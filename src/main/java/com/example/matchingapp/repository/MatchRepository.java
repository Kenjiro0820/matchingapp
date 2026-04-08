package com.example.matchingapp.repository;

import com.example.matchingapp.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByUserAIdOrUserBIdOrderByMatchedAtDesc(Long userAId, Long userBId);

    @Query("""
        select m from Match m
        where
            (m.userAId = :userAId
             and m.userBId = :userBId
             and m.groupProfileAId = :groupProfileAId
             and m.groupProfileBId = :groupProfileBId)
        or
            (m.userAId = :reverseUserAId
             and m.userBId = :reverseUserBId
             and m.groupProfileAId = :reverseGroupProfileAId
             and m.groupProfileBId = :reverseGroupProfileBId)
        """)
    Optional<Match> findExistingMatch(
            @Param("userAId") Long userAId,
            @Param("userBId") Long userBId,
            @Param("groupProfileAId") Long groupProfileAId,
            @Param("groupProfileBId") Long groupProfileBId,
            @Param("reverseUserAId") Long reverseUserAId,
            @Param("reverseUserBId") Long reverseUserBId,
            @Param("reverseGroupProfileAId") Long reverseGroupProfileAId,
            @Param("reverseGroupProfileBId") Long reverseGroupProfileBId
    );
}