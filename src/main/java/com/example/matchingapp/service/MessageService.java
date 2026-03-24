package com.example.matchingapp.service;

import com.example.matchingapp.dto.MessageRequest;
import com.example.matchingapp.dto.MessageResponse;
import com.example.matchingapp.model.Match;
import com.example.matchingapp.model.Message;
import com.example.matchingapp.repository.MatchRepository;
import com.example.matchingapp.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final MatchRepository matchRepository;

    public MessageService(MessageRepository messageRepository, MatchRepository matchRepository) {
        this.messageRepository = messageRepository;
        this.matchRepository = matchRepository;
    }

    public List<MessageResponse> getMessages(Long matchId) {
        return messageRepository.findByMatchIdOrderByCreatedAtAsc(matchId)
                .stream()
                .map(m -> new MessageResponse(
                        m.getId(),
                        m.getSenderUserId(),
                        m.getBody(),
                        m.getIsRead(),
                        m.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public void sendMessage(Long userId, Long matchId, MessageRequest request) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("マッチが存在しません"));

        if (!match.getUserAId().equals(userId) && !match.getUserBId().equals(userId)) {
            throw new IllegalArgumentException("権限がありません");
        }

        Message message = new Message();
        message.setMatchId(matchId);
        message.setSenderUserId(userId);
        message.setBody(request.getBody());
        message.setIsRead(false);
        message.setCreatedAt(LocalDateTime.now());

        messageRepository.save(message);
    }
}