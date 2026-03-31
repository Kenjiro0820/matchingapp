package com.example.matchingapp.repository;

import com.example.matchingapp.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByMatchIdOrderByCreatedAtAsc(Long matchId);

    long countByMatchIdInAndSenderUserIdNotAndIsReadFalse(List<Long> matchIds, Long senderUserId);

    List<Message> findByMatchIdAndSenderUserIdNotAndIsReadFalse(Long matchId, Long senderUserId);

    Optional<Message> findTopByMatchIdOrderByCreatedAtDesc(Long matchId);
}