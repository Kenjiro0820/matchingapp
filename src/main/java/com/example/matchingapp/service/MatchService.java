package com.example.matchingapp.service;

import com.example.matchingapp.dto.MatchResponse;
import com.example.matchingapp.model.GroupProfile;
import com.example.matchingapp.model.Match;
import com.example.matchingapp.model.Message;
import com.example.matchingapp.model.RepresentativeProfile;
import com.example.matchingapp.repository.GroupProfileRepository;
import com.example.matchingapp.repository.MatchRepository;
import com.example.matchingapp.repository.MessageRepository;
import com.example.matchingapp.repository.RepresentativeProfileRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final RepresentativeProfileRepository representativeProfileRepository;
    private final GroupProfileRepository groupProfileRepository;
    private final MessageRepository messageRepository;

    public MatchService(
            MatchRepository matchRepository,
            RepresentativeProfileRepository representativeProfileRepository,
            GroupProfileRepository groupProfileRepository,
            MessageRepository messageRepository
    ) {
        this.matchRepository = matchRepository;
        this.representativeProfileRepository = representativeProfileRepository;
        this.groupProfileRepository = groupProfileRepository;
        this.messageRepository = messageRepository;
    }

    public List<MatchResponse> getMatches(Long userId) {
        List<Match> matches = matchRepository.findByUserAIdOrUserBIdOrderByMatchedAtDesc(userId, userId);
        List<MatchResponse> result = new ArrayList<>();

        for (Match match : matches) {
            Long otherUserId = match.getUserAId().equals(userId) ? match.getUserBId() : match.getUserAId();

            RepresentativeProfile rep = representativeProfileRepository.findByUserId(otherUserId).orElse(null);
            GroupProfile group = groupProfileRepository.findByOwnerUserId(otherUserId).orElse(null);

            Message latestMessage = messageRepository.findTopByMatchIdOrderByCreatedAtDesc(match.getId()).orElse(null);
            String lastMessage = latestMessage != null ? latestMessage.getBody() : null;
            LocalDateTime lastMessageAt = latestMessage != null ? latestMessage.getCreatedAt() : null;

            result.add(new MatchResponse(
                    match.getId(),
                    otherUserId,
                    rep != null ? rep.getNickname() : null,
                    rep != null ? rep.getProfileImageUrl() : null,
                    group != null ? group.getTitle() : null,
                    group != null ? group.getArea() : null,
                    match.getStatus(),
                    match.getMatchedAt(),
                    lastMessage,
                    lastMessageAt
            ));
        }

        return result;
    }
}