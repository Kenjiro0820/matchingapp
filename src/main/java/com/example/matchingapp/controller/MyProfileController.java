package com.example.matchingapp.controller;

import com.example.matchingapp.dto.GroupProfileRequest;
import com.example.matchingapp.dto.GroupProfileResponse;
import com.example.matchingapp.dto.RepresentativeProfileRequest;
import com.example.matchingapp.dto.RepresentativeProfileResponse;
import com.example.matchingapp.service.MyProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me")
@CrossOrigin(origins = "http://localhost:5173")
public class MyProfileController {

    private final MyProfileService myProfileService;

    public MyProfileController(MyProfileService myProfileService) {
        this.myProfileService = myProfileService;
    }

    @GetMapping("/profile")
    public ResponseEntity<RepresentativeProfileResponse> getMyRepresentativeProfile(
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(myProfileService.getMyRepresentativeProfile(userId));
    }

    @PutMapping("/profile")
    public ResponseEntity<RepresentativeProfileResponse> upsertMyRepresentativeProfile(
            @RequestParam Long userId,
            @RequestBody RepresentativeProfileRequest request
    ) {
        return ResponseEntity.ok(myProfileService.upsertMyRepresentativeProfile(userId, request));
    }

    @GetMapping("/group-profile")
    public ResponseEntity<GroupProfileResponse> getMyGroupProfile(
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(myProfileService.getMyGroupProfile(userId));
    }

    @PutMapping("/group-profile")
    public ResponseEntity<GroupProfileResponse> upsertMyGroupProfile(
            @RequestParam Long userId,
            @RequestBody GroupProfileRequest request
    ) {
        return ResponseEntity.ok(myProfileService.upsertMyGroupProfile(userId, request));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}