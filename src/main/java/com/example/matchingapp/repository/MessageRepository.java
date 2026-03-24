package com.example.matchingapp.repository;

import com.example.matchingapp.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByMatchIdOrderByCreatedAtAsc(Long matchId);
}