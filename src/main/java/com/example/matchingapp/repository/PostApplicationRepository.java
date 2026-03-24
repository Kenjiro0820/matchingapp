package com.example.matchingapp.repository;

import com.example.matchingapp.model.PostApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostApplicationRepository extends JpaRepository<PostApplication, Long> {
    List<PostApplication> findByPostId(Long postId);
}
