package com.example.matchingapp.service;

import com.example.matchingapp.model.GroupPost;
import com.example.matchingapp.model.GroupProfile;
import com.example.matchingapp.model.Match;
import com.example.matchingapp.model.PostApplication;
import com.example.matchingapp.repository.GroupPostRepository;
import com.example.matchingapp.repository.GroupProfileRepository;
import com.example.matchingapp.repository.MatchRepository;
import com.example.matchingapp.repository.PostApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class ApplicationService {

    private final PostApplicationRepository postApplicationRepository;
    private final GroupPostRepository groupPostRepository;
    private final GroupProfileRepository groupProfileRepository;
    private final MatchRepository matchRepository;

    public ApplicationService(
            PostApplicationRepository postApplicationRepository,
            GroupPostRepository groupPostRepository,
            GroupProfileRepository groupProfileRepository,
            MatchRepository matchRepository
    ) {
        this.postApplicationRepository = postApplicationRepository;
        this.groupPostRepository = groupPostRepository;
        this.groupProfileRepository = groupProfileRepository;
        this.matchRepository = matchRepository;
    }

    public PostApplication apply(PostApplication application) {
        if (application.getPostId() == null || application.getApplicantUserId() == null) {
            throw new IllegalArgumentException("応募情報が不正です");
        }

        PostApplication latest = postApplicationRepository
                .findTopByPostIdAndApplicantUserIdOrderByCreatedAtDesc(
                        application.getPostId(),
                        application.getApplicantUserId()
                )
                .orElse(null);

        if (latest != null) {
            if ("PENDING".equals(latest.getStatus())) {
                throw new IllegalArgumentException("この募集にはすでに応募済みです");
            }
            if ("APPROVED".equals(latest.getStatus())) {
                throw new IllegalArgumentException("この募集への応募は承認済みです");
            }
        }

        if (application.getStatus() == null || application.getStatus().isBlank()) {
            application.setStatus("PENDING");
        }

        return postApplicationRepository.save(application);
    }

    @Transactional(readOnly = true)
    public List<PostApplication> getByPost(Long postId) {
        return postApplicationRepository.findByPostId(postId);
    }

    @Transactional(readOnly = true)
    public List<PostApplication> getReceivedApplications(Long userId) {
        List<GroupPost> myPosts = groupPostRepository.findByOrganizerUserIdOrderByCreatedAtDesc(userId);
        if (myPosts.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> postIds = myPosts.stream()
                .map(GroupPost::getId)
                .toList();

        return postApplicationRepository.findByPostIdInAndStatusOrderByCreatedAtDesc(postIds, "PENDING");
    }

    @Transactional(readOnly = true)
    public List<Long> getActiveAppliedPostIds(Long userId) {
        List<PostApplication> applications = postApplicationRepository.findByApplicantUserIdOrderByCreatedAtDesc(userId);

        List<Long> result = new java.util.ArrayList<>();
        java.util.Set<Long> seenPostIds = new java.util.HashSet<>();

        for (PostApplication application : applications) {
            Long postId = application.getPostId();
            if (postId == null || seenPostIds.contains(postId)) {
                continue;
            }

            seenPostIds.add(postId);

            if ("PENDING".equals(application.getStatus()) || "APPROVED".equals(application.getStatus())) {
                result.add(postId);
            }
        }

        return result;
    }

    public void approve(Long id) {
        PostApplication application = postApplicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("応募が存在しません"));

        GroupPost post = groupPostRepository.findById(application.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("募集が存在しません"));

        GroupProfile organizerGroupProfile = groupProfileRepository.findByOwnerUserId(post.getOrganizerUserId())
                .orElseThrow(() -> new IllegalArgumentException("主催者のグループプロフィールが存在しません"));

        GroupProfile applicantGroupProfile = groupProfileRepository.findByOwnerUserId(application.getApplicantUserId())
                .orElseThrow(() -> new IllegalArgumentException("応募者のグループプロフィールが存在しません"));

        application.setStatus("APPROVED");
        postApplicationRepository.save(application);

        Match existingMatch = matchRepository.findExistingMatch(
                post.getOrganizerUserId(),
                application.getApplicantUserId(),
                organizerGroupProfile.getId(),
                applicantGroupProfile.getId(),
                application.getApplicantUserId(),
                post.getOrganizerUserId(),
                applicantGroupProfile.getId(),
                organizerGroupProfile.getId()
        ).orElse(null);

        if (existingMatch == null) {
            Match match = new Match();
            match.setUserAId(post.getOrganizerUserId());
            match.setUserBId(application.getApplicantUserId());
            match.setGroupProfileAId(organizerGroupProfile.getId());
            match.setGroupProfileBId(applicantGroupProfile.getId());
            match.setStatus("MATCHED");
            matchRepository.save(match);
        }

        post.setStatus("CLOSED");
        groupPostRepository.save(post);
    }

    public void reject(Long id) {
        updateStatus(id, "REJECTED");
    }

    private void updateStatus(Long id, String status) {
        PostApplication application = postApplicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("応募が存在しません"));

        application.setStatus(status);
        postApplicationRepository.save(application);
    }
}