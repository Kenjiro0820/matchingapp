package com.example.matchingapp.service;

import com.example.matchingapp.model.PostApplication;
import com.example.matchingapp.repository.PostApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    private final PostApplicationRepository postApplicationRepository;

    public ApplicationService(PostApplicationRepository postApplicationRepository) {
        this.postApplicationRepository = postApplicationRepository;
    }

    public PostApplication apply(PostApplication application) {
        if (application.getStatus() == null || application.getStatus().isBlank()) {
            application.setStatus("PENDING");
        }
        return postApplicationRepository.save(application);
    }

    public List<PostApplication> getByPost(Long postId) {
        return postApplicationRepository.findByPostId(postId);
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
