package com.example.matchingapp.service;

import com.example.matchingapp.model.PostApplication;
import com.example.matchingapp.repository.PostApplicationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService {

    private final PostApplicationRepository repository;

    public ApplicationService(PostApplicationRepository repository) {
        this.repository = repository;
    }

    public PostApplication apply(PostApplication app) {
        app.setStatus("PENDING");
        app.setCreatedAt(LocalDateTime.now());
        return repository.save(app);
    }

    public List<PostApplication> getByPost(Long postId) {
        return repository.findByPostId(postId);
    }

    public void approve(Long id) {
        PostApplication app = repository.findById(id).orElseThrow();
        app.setStatus("APPROVED");
        repository.save(app);
    }

    public void reject(Long id) {
        PostApplication app = repository.findById(id).orElseThrow();
        app.setStatus("REJECTED");
        repository.save(app);
    }
}
