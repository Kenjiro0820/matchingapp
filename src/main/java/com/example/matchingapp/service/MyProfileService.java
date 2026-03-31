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
        User user = getUserOrThrow(userId);

        RepresentativeProfile profile = representativeProfileRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultRepresentativeProfile(user));

        return toRepresentativeProfileResponse(profile);
    }

    public RepresentativeProfileResponse upsertMyRepresentativeProfile(Long userId, RepresentativeProfileRequest request) {
        User user = getUserOrThrow(userId);

        RepresentativeProfile profile = representativeProfileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    RepresentativeProfile newProfile = new RepresentativeProfile();
                    newProfile.setUserId(userId);
                    newProfile.setNickname(user.getName());
                    newProfile.setIsActive(true);
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
        User user = getUserOrThrow(userId);

        GroupProfile groupProfile = groupProfileRepository.findByOwnerUserId(userId)
                .orElseGet(() -> createDefaultGroupProfile(user));

        return toGroupProfileResponse(groupProfile);
    }

    public GroupProfileResponse upsertMyGroupProfile(Long userId, GroupProfileRequest request) {
        User user = getUserOrThrow(userId);

        GroupProfile groupProfile = groupProfileRepository.findByOwnerUserId(userId)
                .orElseGet(() -> {
                    GroupProfile newGroupProfile = new GroupProfile();
                    newGroupProfile.setOwnerUserId(userId);
                    newGroupProfile.setTitle(user.getName() + "さんのグループ");
                    newGroupProfile.setStatus("ACTIVE");
                    newGroupProfile.setMaleCount(0);
                    newGroupProfile.setFemaleCount(0);
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

    private User getUserOrThrow(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが存在しません"));

        if (user.getId() == null) {
            throw new IllegalArgumentException("ユーザーが不正です");
        }

        return user;
    }

    private RepresentativeProfile createDefaultRepresentativeProfile(User user) {
        RepresentativeProfile profile = new RepresentativeProfile();
        profile.setUserId(user.getId());
        profile.setNickname(user.getName());
        profile.setProfileImageUrl(null);
        profile.setBio(null);
        profile.setAgeRange(null);
        profile.setOccupation(null);
        profile.setDrinkingLevel(null);
        profile.setPersonalityTags(null);
        profile.setPreferredArea(null);
        profile.setIsActive(true);
        return representativeProfileRepository.save(profile);
    }

    private GroupProfile createDefaultGroupProfile(User user) {
        GroupProfile groupProfile = new GroupProfile();
        groupProfile.setOwnerUserId(user.getId());
        groupProfile.setTitle(user.getName() + "さんのグループ");
        groupProfile.setGroupImageUrl(null);
        groupProfile.setIntroduction(null);
        groupProfile.setArea(null);
        groupProfile.setPreferredArea(null);
        groupProfile.setMaleCount(0);
        groupProfile.setFemaleCount(0);
        groupProfile.setAgeMin(null);
        groupProfile.setAgeMax(null);
        groupProfile.setPreferredAgeMin(null);
        groupProfile.setPreferredAgeMax(null);
        groupProfile.setBudgetPerPerson(null);
        groupProfile.setMeetingStyle(null);
        groupProfile.setAvailableDays(null);
        groupProfile.setPreferredGroupDescription(null);
        groupProfile.setStatus("ACTIVE");
        return groupProfileRepository.save(groupProfile);
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