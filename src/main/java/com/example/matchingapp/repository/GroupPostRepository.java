package com.example.matchingapp.repository;

import com.example.matchingapp.model.GroupPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface GroupPostRepository extends JpaRepository<GroupPost, Long> {

    List<GroupPost> findByOrganizerUserIdOrderByCreatedAtDesc(Long organizerUserId);

    List<GroupPost> findByOrganizerUserIdAndStatusOrderByCreatedAtDesc(Long organizerUserId, String status);

    List<GroupPost> findByStatusAndScheduledAtAfterOrderByScheduledAtAsc(String status, LocalDateTime now);
}