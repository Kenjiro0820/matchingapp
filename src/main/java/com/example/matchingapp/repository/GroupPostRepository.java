package com.example.matchingapp.repository;

import com.example.matchingapp.model.GroupPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupPostRepository extends JpaRepository<GroupPost, Long> {
    List<GroupPost> findByOrganizerUserId(Long userId);
}
