package com.example.matchingapp.service;

import com.example.matchingapp.dto.GroupProfileRequest;
import com.example.matchingapp.dto.GroupProfileResponse;
import com.example.matchingapp.dto.RepresentativeProfileRequest;
import com.example.matchingapp.dto.RepresentativeProfileResponse;
import com.example.matchingapp.model.GroupProfile;
import com.example.matchingapp.model.RepresentativeProfile;
import com.example.matchingapp.model.User;
import com.example.matchingapp.repository.GroupProfileRepository;
import com.example.matchingapp.repository.RepresentativeProfileRepository;
import com.example.matchingapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MyProfileService {

    private final UserRepository userRepository;
    private final RepresentativeProfileRepository representativeProfileRepository;
    private final GroupProfileRepository groupProfileRepository;

    public MyProfileService(
            UserRepository userRepository,
            RepresentativeProfileRepository representativeProfileRepository,
            GroupProfileRepository groupProfileRepository
    ) {
        this.userRepository = userRepository;
        this.representativeProfileRepository = representativeProfileRepository;
        this.groupProfileRepository = groupProfileRepository;
    }

    @Transactional(readOnly = true)
    public RepresentativeProfileResponse getMyRepresentativeProfile(Long userId) {
        validateUserExists(userId);

        RepresentativeProfile profile = representativeProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("代表者プロフィールが存在しません"));

        return toRepresentativeProfileResponse(profile);
    }

    public RepresentativeProfileResponse upsertMyRepresentativeProfile(Long userId, RepresentativeProfileRequest request) {
        validateUserExists(userId);

        RepresentativeProfile profile = representativeProfileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    RepresentativeProfile newProfile = new RepresentativeProfile();
                    newProfile.setUserId(userId);
                    return newProfile;
                });

        if (request.getNickname() == null || request.getNickname().trim().isEmpty()) {
            throw new IllegalArgumentException("nickname は必須です");
        }

        profile.setNickname(request.getNickname().trim());
        profile.setProfileImageUrl(request.getProfileImageUrl());
        profile.setBio(request.getBio());
        profile.setAgeRange(request.getAgeRange());
        profile.setOccupation(request.getOccupation());
        profile.setDrinkingLevel(request.getDrinkingLevel());
        profile.setPersonalityTags(request.getPersonalityTags());
        profile.setPreferredArea(request.getPreferredArea());
        profile.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        RepresentativeProfile saved = representativeProfileRepository.save(profile);
        return toRepresentativeProfileResponse(saved);
    }

    @Transactional(readOnly = true)
    public GroupProfileResponse getMyGroupProfile(Long userId) {
        validateUserExists(userId);

        GroupProfile groupProfile = groupProfileRepository.findByOwnerUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("グループプロフィールが存在しません"));

        return toGroupProfileResponse(groupProfile);
    }

    public GroupProfileResponse upsertMyGroupProfile(Long userId, GroupProfileRequest request) {
        validateUserExists(userId);

        GroupProfile groupProfile = groupProfileRepository.findByOwnerUserId(userId)
                .orElseGet(() -> {
                    GroupProfile newGroupProfile = new GroupProfile();
                    newGroupProfile.setOwnerUserId(userId);
                    return newGroupProfile;
                });

        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("title は必須です");
        }

        groupProfile.setTitle(request.getTitle().trim());
        groupProfile.setGroupImageUrl(request.getGroupImageUrl());
        groupProfile.setIntroduction(request.getIntroduction());
        groupProfile.setArea(request.getArea());
        groupProfile.setPreferredArea(request.getPreferredArea());
        groupProfile.setMaleCount(request.getMaleCount() != null ? request.getMaleCount() : 0);
        groupProfile.setFemaleCount(request.getFemaleCount() != null ? request.getFemaleCount() : 0);
        groupProfile.setAgeMin(request.getAgeMin());
        groupProfile.setAgeMax(request.getAgeMax());
        groupProfile.setPreferredAgeMin(request.getPreferredAgeMin());
        groupProfile.setPreferredAgeMax(request.getPreferredAgeMax());
        groupProfile.setBudgetPerPerson(request.getBudgetPerPerson());
        groupProfile.setMeetingStyle(request.getMeetingStyle());
        groupProfile.setAvailableDays(request.getAvailableDays());
        groupProfile.setPreferredGroupDescription(request.getPreferredGroupDescription());
        groupProfile.setStatus(request.getStatus() != null ? request.getStatus() : "ACTIVE");

        GroupProfile saved = groupProfileRepository.save(groupProfile);
        return toGroupProfileResponse(saved);
    }

    private void validateUserExists(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが存在しません"));

        if (user.getId() == null) {
            throw new IllegalArgumentException("ユーザーが不正です");
        }
    }

    private RepresentativeProfileResponse toRepresentativeProfileResponse(RepresentativeProfile profile) {
        return new RepresentativeProfileResponse(
                profile.getId(),
                profile.getUserId(),
                profile.getNickname(),
                profile.getProfileImageUrl(),
                profile.getBio(),
                profile.getAgeRange(),
                profile.getOccupation(),
                profile.getDrinkingLevel(),
                profile.getPersonalityTags(),
                profile.getPreferredArea(),
                profile.getIsActive()
        );
    }

    private GroupProfileResponse toGroupProfileResponse(GroupProfile profile) {
        return new GroupProfileResponse(
                profile.getId(),
                profile.getOwnerUserId(),
                profile.getTitle(),
                profile.getGroupImageUrl(),
                profile.getIntroduction(),
                profile.getArea(),
                profile.getPreferredArea(),
                profile.getMaleCount(),
                profile.getFemaleCount(),
                profile.getAgeMin(),
                profile.getAgeMax(),
                profile.getPreferredAgeMin(),
                profile.getPreferredAgeMax(),
                profile.getBudgetPerPerson(),
                profile.getMeetingStyle(),
                profile.getAvailableDays(),
                profile.getPreferredGroupDescription(),
                profile.getStatus()
        );
    }
}
