package com.example.matchingapp.service;

import com.example.matchingapp.dto.MessageRequest;
import com.example.matchingapp.dto.MessageResponse;
import com.example.matchingapp.model.Match;
import com.example.matchingapp.model.Message;
import com.example.matchingapp.repository.MatchRepository;
import com.example.matchingapp.repository.MessageRepository;
import org.springframework.stereotype.Service;

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
                .map(message -> new MessageResponse(
                        message.getId(),
                        message.getSenderUserId(),
                        message.getBody(),
                        message.getIsRead(),
                        message.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public void sendMessage(Long userId, Long matchId, MessageRequest request) {
        if (request.getBody() == null || request.getBody().trim().isEmpty()) {
            throw new IllegalArgumentException("メッセージ本文は必須です");
        }

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("マッチが存在しません"));

        if (!match.getUserAId().equals(userId) && !match.getUserBId().equals(userId)) {
            throw new IllegalArgumentException("権限がありません");
        }

        Message message = new Message();
        message.setMatchId(matchId);
        message.setSenderUserId(userId);
        message.setBody(request.getBody().trim());
        message.setIsRead(false);

        messageRepository.save(message);
    }

    public long countUnreadMessages(Long userId) {
        List<Long> matchIds = matchRepository.findByUserAIdOrUserBIdOrderByMatchedAtDesc(userId, userId)
                .stream()
                .map(Match::getId)
                .toList();

        if (matchIds.isEmpty()) {
            return 0L;
        }

        return messageRepository.countByMatchIdInAndSenderUserIdNotAndIsReadFalse(matchIds, userId);
    }

    public void markAsRead(Long userId, Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("マッチが存在しません"));

        if (!match.getUserAId().equals(userId) && !match.getUserBId().equals(userId)) {
            throw new IllegalArgumentException("権限がありません");
        }

        List<Message> unreadMessages =
                messageRepository.findByMatchIdAndSenderUserIdNotAndIsReadFalse(matchId, userId);

        if (unreadMessages.isEmpty()) {
            return;
        }

        unreadMessages.forEach(message -> message.setIsRead(true));
        messageRepository.saveAll(unreadMessages);
    }
}
