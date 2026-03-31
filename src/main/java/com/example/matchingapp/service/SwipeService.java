package com.example.matchingapp.service;

import com.example.matchingapp.dto.SwipeCandidateResponse;
import com.example.matchingapp.dto.SwipeRequest;
import com.example.matchingapp.dto.SwipeResultResponse;
import com.example.matchingapp.model.GroupProfile;
import com.example.matchingapp.model.Match;
import com.example.matchingapp.model.RepresentativeProfile;
import com.example.matchingapp.model.SwipeAction;
import com.example.matchingapp.model.User;
import com.example.matchingapp.repository.GroupProfileRepository;
import com.example.matchingapp.repository.MatchRepository;
import com.example.matchingapp.repository.RepresentativeProfileRepository;
import com.example.matchingapp.repository.SwipeActionRepository;
import com.example.matchingapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class SwipeService {

    private final UserRepository userRepository;
    private final RepresentativeProfileRepository representativeProfileRepository;
    private final GroupProfileRepository groupProfileRepository;
    private final SwipeActionRepository swipeActionRepository;
    private final MatchRepository matchRepository;

    public SwipeService(
            UserRepository userRepository,
            RepresentativeProfileRepository representativeProfileRepository,
            GroupProfileRepository groupProfileRepository,
            SwipeActionRepository swipeActionRepository,
            MatchRepository matchRepository) {
        this.userRepository = userRepository;
        this.representativeProfileRepository = representativeProfileRepository;
        this.groupProfileRepository = groupProfileRepository;
        this.swipeActionRepository = swipeActionRepository;
        this.matchRepository = matchRepository;
    }

    @Transactional(readOnly = true)
    public List<SwipeCandidateResponse> getCandidates(Long userId) {
        validateUserExists(userId);
        validateMyProfilesExist(userId);

        GroupProfile myGroupProfile = getMyGroupProfile(userId);
        List<RepresentativeProfile> repProfiles = representativeProfileRepository
                .findByIsActiveTrueAndUserIdNot(userId);
        List<SwipeCandidateResponse> responses = new ArrayList<>();

        for (RepresentativeProfile repProfile : repProfiles) {
            GroupProfile groupProfile = groupProfileRepository.findByOwnerUserId(repProfile.getUserId()).orElse(null);
            if (groupProfile == null) {
                continue;
            }

            if (!"ACTIVE".equals(groupProfile.getStatus())) {
                continue;
            }

            boolean alreadySwiped = swipeActionRepository
                    .existsByFromUserIdAndToUserIdAndFromGroupProfileIdAndToGroupProfileId(
                            userId,
                            repProfile.getUserId(),
                            myGroupProfile.getId(),
                            groupProfile.getId());

            if (alreadySwiped) {
                continue;
            }

            responses.add(toSwipeCandidateResponse(repProfile, groupProfile));
        }

        return responses;
    }

    @Transactional(readOnly = true)
    public List<SwipeCandidateResponse> getIncomingLikes(Long userId) {
        validateUserExists(userId);
        validateMyProfilesExist(userId);

        GroupProfile myGroupProfile = getMyGroupProfile(userId);
        List<SwipeAction> incomingLikes = swipeActionRepository.findByToUserIdAndActionOrderByIdDesc(userId, "LIKE");

        List<SwipeCandidateResponse> responses = new ArrayList<>();
        Set<Long> addedUserIds = new LinkedHashSet<>();

        for (SwipeAction incomingLike : incomingLikes) {
            if (addedUserIds.contains(incomingLike.getFromUserId())) {
                continue;
            }

            boolean alreadyResponded = swipeActionRepository
                    .existsByFromUserIdAndToUserIdAndFromGroupProfileIdAndToGroupProfileId(
                            userId,
                            incomingLike.getFromUserId(),
                            myGroupProfile.getId(),
                            incomingLike.getFromGroupProfileId());

            if (alreadyResponded) {
                continue;
            }

            RepresentativeProfile repProfile = representativeProfileRepository
                    .findByUserId(incomingLike.getFromUserId()).orElse(null);

            if (repProfile == null || !Boolean.TRUE.equals(repProfile.getIsActive())) {
                continue;
            }

            GroupProfile groupProfile = groupProfileRepository.findById(incomingLike.getFromGroupProfileId())
                    .orElse(null);

            if (groupProfile == null) {
                continue;
            }

            if (!incomingLike.getFromUserId().equals(groupProfile.getOwnerUserId())) {
                continue;
            }

            if (!"ACTIVE".equals(groupProfile.getStatus())) {
                continue;
            }

            responses.add(toSwipeCandidateResponse(repProfile, groupProfile));
            addedUserIds.add(incomingLike.getFromUserId());
        }

        return responses;
    }

    public SwipeResultResponse swipe(Long userId, SwipeRequest request) {
        validateUserExists(userId);
        validateMyProfilesExist(userId);

        if (request.getTargetUserId() == null) {
            throw new IllegalArgumentException("targetUserId は必須です");
        }
        if (request.getTargetGroupProfileId() == null) {
            throw new IllegalArgumentException("targetGroupProfileId は必須です");
        }
        if (request.getAction() == null || request.getAction().trim().isEmpty()) {
            throw new IllegalArgumentException("action は必須です");
        }

        String action = request.getAction().trim().toUpperCase();
        if (!"LIKE".equals(action) && !"NOPE".equals(action)) {
            throw new IllegalArgumentException("action は LIKE または NOPE を指定してください");
        }

        if (userId.equals(request.getTargetUserId())) {
            throw new IllegalArgumentException("自分自身にはスワイプできません");
        }

        RepresentativeProfile targetRepProfile = representativeProfileRepository.findByUserId(request.getTargetUserId())
                .orElseThrow(() -> new IllegalArgumentException("相手の代表者プロフィールが存在しません"));

        GroupProfile targetGroupProfile = groupProfileRepository.findById(request.getTargetGroupProfileId())
                .orElseThrow(() -> new IllegalArgumentException("相手のグループプロフィールが存在しません"));

        if (!targetRepProfile.getUserId().equals(targetGroupProfile.getOwnerUserId())) {
            throw new IllegalArgumentException("相手プロフィールとグループプロフィールの所有者が一致しません");
        }

        if (!Boolean.TRUE.equals(targetRepProfile.getIsActive())) {
            throw new IllegalArgumentException("相手プロフィールは非公開です");
        }

        if (!"ACTIVE".equals(targetGroupProfile.getStatus())) {
            throw new IllegalArgumentException("相手グループプロフィールは募集停止中です");
        }

        GroupProfile myGroupProfile = getMyGroupProfile(userId);

        boolean alreadySwiped = swipeActionRepository
                .existsByFromUserIdAndToUserIdAndFromGroupProfileIdAndToGroupProfileId(
                        userId,
                        request.getTargetUserId(),
                        myGroupProfile.getId(),
                        request.getTargetGroupProfileId());

        if (alreadySwiped) {
            throw new IllegalArgumentException("すでにスワイプ済みです");
        }

        SwipeAction swipeAction = new SwipeAction();
        swipeAction.setFromUserId(userId);
        swipeAction.setToUserId(request.getTargetUserId());
        swipeAction.setFromGroupProfileId(myGroupProfile.getId());
        swipeAction.setToGroupProfileId(request.getTargetGroupProfileId());
        swipeAction.setAction(action);
        swipeActionRepository.save(swipeAction);

        if ("NOPE".equals(action)) {
            return new SwipeResultResponse(false, null, "スワイプを保存しました");
        }

        SwipeAction reverseLike = swipeActionRepository
                .findByFromUserIdAndToUserIdAndFromGroupProfileIdAndToGroupProfileId(
                        request.getTargetUserId(),
                        userId,
                        request.getTargetGroupProfileId(),
                        myGroupProfile.getId())
                .orElse(null);

        if (reverseLike != null && "LIKE".equals(reverseLike.getAction())) {
            Match existingMatch = matchRepository.findExistingMatch(
                    userId,
                    request.getTargetUserId(),
                    myGroupProfile.getId(),
                    request.getTargetGroupProfileId(),
                    request.getTargetUserId(),
                    userId,
                    request.getTargetGroupProfileId(),
                    myGroupProfile.getId()).orElse(null);

            if (existingMatch != null) {
                return new SwipeResultResponse(true, existingMatch.getId(), "すでにマッチ済みです");
            }

            Match match = new Match();
            match.setUserAId(userId);
            match.setUserBId(request.getTargetUserId());
            match.setGroupProfileAId(myGroupProfile.getId());
            match.setGroupProfileBId(request.getTargetGroupProfileId());
            match.setStatus("MATCHED");

            Match savedMatch = matchRepository.save(match);
            return new SwipeResultResponse(true, savedMatch.getId(), "マッチしました");
        }

        return new SwipeResultResponse(false, null, "スワイプを保存しました");
    }

    private SwipeCandidateResponse toSwipeCandidateResponse(
            RepresentativeProfile repProfile,
            GroupProfile groupProfile) {
        return new SwipeCandidateResponse(
                repProfile.getUserId(),
                groupProfile.getId(),
                repProfile.getNickname(),
                repProfile.getProfileImageUrl(),
                repProfile.getBio(),
                repProfile.getAgeRange(),
                repProfile.getOccupation(),
                repProfile.getPersonalityTags(),
                repProfile.getPreferredArea(),
                groupProfile.getTitle(),
                groupProfile.getGroupImageUrl(),
                groupProfile.getIntroduction(),
                groupProfile.getArea(),
                groupProfile.getMaleCount(),
                groupProfile.getFemaleCount(),
                groupProfile.getAgeMin(),
                groupProfile.getAgeMax(),
                groupProfile.getBudgetPerPerson(),
                groupProfile.getMeetingStyle(),
                groupProfile.getAvailableDays(),
                groupProfile.getPreferredGroupDescription());
    }

    private void validateUserExists(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが存在しません"));

        if (user.getId() == null) {
            throw new IllegalArgumentException("ユーザーが不正です");
        }
    }

    private void validateMyProfilesExist(Long userId) {
        representativeProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("自分の代表者プロフィールを先に作成してください"));

        groupProfileRepository.findByOwnerUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("自分のグループプロフィールを先に作成してください"));
    }

    private GroupProfile getMyGroupProfile(Long userId) {
        return groupProfileRepository.findByOwnerUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("自分のグループプロフィールが存在しません"));
    }
}