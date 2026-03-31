package com.example.matchingapp.service;

import com.example.matchingapp.model.GroupPost;
import com.example.matchingapp.model.PostApplication;
import com.example.matchingapp.repository.GroupPostRepository;
import com.example.matchingapp.repository.PostApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ApplicationService {

    private final PostApplicationRepository postApplicationRepository;
    private final GroupPostRepository groupPostRepository;

    public ApplicationService(
            PostApplicationRepository postApplicationRepository,
            GroupPostRepository groupPostRepository) {
        this.postApplicationRepository = postApplicationRepository;
        this.groupPostRepository = groupPostRepository;
    }

    public PostApplication apply(PostApplication application) {
        if (application.getPostId() == null || application.getApplicantUserId() == null) {
            throw new IllegalArgumentException("応募情報が不正です");
        }

        PostApplication latest = postApplicationRepository
                .findTopByPostIdAndApplicantUserIdOrderByCreatedAtDesc(
                        application.getPostId(),
                        application.getApplicantUserId())
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

    public List<PostApplication> getByPost(Long postId) {
        return postApplicationRepository.findByPostId(postId);
    }

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

    public void approve(Long id) {
        updateStatus(id, "APPROVED");
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