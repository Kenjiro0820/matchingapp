package com.example.matchingapp.repository;

import com.example.matchingapp.model.PostApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostApplicationRepository extends JpaRepository<PostApplication, Long> {

    List<PostApplication> findByPostId(Long postId);

    List<PostApplication> findByPostIdInOrderByCreatedAtDesc(List<Long> postIds);

    Optional<PostApplication> findTopByPostIdAndApplicantUserIdOrderByCreatedAtDesc(Long postId, Long applicantUserId);

    List<PostApplication> findByPostIdInAndStatusOrderByCreatedAtDesc(List<Long> postIds, String status);

    List<PostApplication> findByApplicantUserIdOrderByCreatedAtDesc(Long applicantUserId);
}