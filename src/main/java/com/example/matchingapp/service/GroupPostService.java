package com.example.matchingapp.service;

import com.example.matchingapp.model.GroupPost;
import com.example.matchingapp.repository.GroupPostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GroupPostService {

    private final GroupPostRepository groupPostRepository;

    public GroupPostService(GroupPostRepository groupPostRepository) {
        this.groupPostRepository = groupPostRepository;
    }

    public GroupPost create(GroupPost post) {
        if (post.getStatus() == null || post.getStatus().isBlank()) {
            post.setStatus("OPEN");
        }
        return groupPostRepository.save(post);
    }

    public List<GroupPost> getAll() {
        return groupPostRepository.findByStatusAndScheduledAtAfterOrderByScheduledAtAsc(
                "OPEN",
                LocalDateTime.now()
        );
    }

    public List<GroupPost> getMyPosts(Long userId) {
        return groupPostRepository.findByOrganizerUserIdAndStatusOrderByCreatedAtDesc(userId, "OPEN");
    }

    public void close(Long id) {
        GroupPost post = groupPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("投稿が存在しません"));

        post.setStatus("CLOSED");
        groupPostRepository.save(post);
    }
}