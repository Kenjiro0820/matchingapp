package com.example.matchingapp.controller;

import com.example.matchingapp.dto.MatchResponse;
import com.example.matchingapp.dto.MessageRequest;
import com.example.matchingapp.dto.MessageResponse;
import com.example.matchingapp.service.MatchService;
import com.example.matchingapp.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matches")
public class MatchController {

    private final MatchService matchService;
    private final MessageService messageService;

    public MatchController(MatchService matchService, MessageService messageService) {
        this.matchService = matchService;
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<List<MatchResponse>> getMatches(@RequestParam Long userId) {
        return ResponseEntity.ok(matchService.getMatches(userId));
    }

    @GetMapping("/{matchId}/messages")
    public ResponseEntity<List<MessageResponse>> getMessages(@PathVariable Long matchId) {
        return ResponseEntity.ok(messageService.getMessages(matchId));
    }

    @PostMapping("/{matchId}/messages")
    public ResponseEntity<String> sendMessage(
            @RequestParam Long userId,
            @PathVariable Long matchId,
            @RequestBody MessageRequest request
    ) {
        messageService.sendMessage(userId, matchId, request);
        return ResponseEntity.ok("送信しました");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
