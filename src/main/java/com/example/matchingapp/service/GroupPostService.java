package com.example.matchingapp.service;

import com.example.matchingapp.model.GroupPost;
import com.example.matchingapp.repository.GroupPostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupPostService {

    private final GroupPostRepository repository;

    public GroupPostService(GroupPostRepository repository) {
        this.repository = repository;
    }

    public GroupPost create(GroupPost post) {
        post.setStatus("OPEN");
        return repository.save(post);
    }

    public List<GroupPost> getAll() {
        return repository.findAll();
    }

    public List<GroupPost> getMyPosts(Long userId) {
        return repository.findByOrganizerUserId(userId);
    }

    public void close(Long postId) {
        GroupPost post = repository.findById(postId).orElseThrow();
        post.setStatus("CLOSED");
        repository.save(post);
    }
}
