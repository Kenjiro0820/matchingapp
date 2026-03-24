package com.example.matchingapp.service;

import com.example.matchingapp.dto.MatchResponse;
import com.example.matchingapp.model.GroupProfile;
import com.example.matchingapp.model.Match;
import com.example.matchingapp.model.RepresentativeProfile;
import com.example.matchingapp.repository.GroupProfileRepository;
import com.example.matchingapp.repository.MatchRepository;
import com.example.matchingapp.repository.RepresentativeProfileRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final RepresentativeProfileRepository representativeProfileRepository;
    private final GroupProfileRepository groupProfileRepository;

    public MatchService(
            MatchRepository matchRepository,
            RepresentativeProfileRepository representativeProfileRepository,
            GroupProfileRepository groupProfileRepository
    ) {
        this.matchRepository = matchRepository;
        this.representativeProfileRepository = representativeProfileRepository;
        this.groupProfileRepository = groupProfileRepository;
    }

    public List<MatchResponse> getMatches(Long userId) {
        List<Match> matches = matchRepository.findByUserAIdOrUserBIdOrderByMatchedAtDesc(userId, userId);
        List<MatchResponse> result = new ArrayList<>();

        for (Match match : matches) {
            Long otherUserId = match.getUserAId().equals(userId)
                    ? match.getUserBId()
                    : match.getUserAId();

            RepresentativeProfile rep = representativeProfileRepository
                    .findByUserId(otherUserId)
                    .orElse(null);

            GroupProfile group = groupProfileRepository
                    .findByOwnerUserId(otherUserId)
                    .orElse(null);

            result.add(new MatchResponse(
                    match.getId(),
                    otherUserId,
                    rep != null ? rep.getNickname() : null,
                    rep != null ? rep.getProfileImageUrl() : null,
                    group != null ? group.getTitle() : null,
                    group != null ? group.getArea() : null,
                    match.getStatus(),
                    match.getMatchedAt()
            ));
        }

        return result;
    }
}