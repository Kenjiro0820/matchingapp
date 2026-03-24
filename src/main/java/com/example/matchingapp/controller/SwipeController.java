package com.example.matchingapp.controller;

import com.example.matchingapp.dto.SwipeCandidateResponse;
import com.example.matchingapp.dto.SwipeRequest;
import com.example.matchingapp.dto.SwipeResultResponse;
import com.example.matchingapp.service.SwipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/swipe")
@CrossOrigin(origins = "http://localhost:5173")
public class SwipeController {

    private final SwipeService swipeService;

    public SwipeController(SwipeService swipeService) {
        this.swipeService = swipeService;
    }

    @GetMapping("/candidates")
    public ResponseEntity<List<SwipeCandidateResponse>> getCandidates(
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(swipeService.getCandidates(userId));
    }

    @PostMapping
    public ResponseEntity<SwipeResultResponse> swipe(
            @RequestParam Long userId,
            @RequestBody SwipeRequest request
    ) {
        return ResponseEntity.ok(swipeService.swipe(userId, request));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}