package com.example.inovaTest.controllers;

import com.example.inovaTest.dtos.user.posts.PostResponseDto;
import com.example.inovaTest.models.UserModel;
import com.example.inovaTest.repositories.UserRepository;
import com.example.inovaTest.services.FeedService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;
    @Autowired
    private UserRepository repository;

    @GetMapping("/{id}")
    public List<PostResponseDto> getFeed(@PathVariable UUID id) {
        UserModel user  = repository.getById(id);
        return feedService.getUserFeed(user);
    }
}
