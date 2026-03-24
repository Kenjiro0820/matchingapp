package com.example.matchingapp.controller;

import com.example.matchingapp.model.GroupPost;
import com.example.matchingapp.service.GroupPostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class GroupPostController {

    private final GroupPostService service;

    public GroupPostController(GroupPostService service) {
        this.service = service;
    }

    @PostMapping
    public GroupPost create(@RequestBody GroupPost post) {
        return service.create(post);
    }

    @GetMapping
    public List<GroupPost> getAll() {
        return service.getAll();
    }

    @GetMapping("/my")
    public List<GroupPost> getMy(@RequestParam Long userId) {
        return service.getMyPosts(userId);
    }

    @PostMapping("/{id}/close")
    public void close(@PathVariable Long id) {
        service.close(id);
    }
}
