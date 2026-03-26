package com.example.matchingapp.controller;

import com.example.matchingapp.model.PostApplication;
import com.example.matchingapp.service.ApplicationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService service;

    public ApplicationController(ApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public PostApplication apply(@RequestBody PostApplication application) {
        return service.apply(application);
    }

    @GetMapping("/post/{postId}")
    public List<PostApplication> get(@PathVariable Long postId) {
        return service.getByPost(postId);
    }

    @PostMapping("/{id}/approve")
    public void approve(@PathVariable Long id) {
        service.approve(id);
    }

    @PostMapping("/{id}/reject")
    public void reject(@PathVariable Long id) {
        service.reject(id);
    }
}
