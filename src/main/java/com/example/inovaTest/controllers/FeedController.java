package com.example.inovaTest.controllers;

import com.example.inovaTest.dtos.user.posts.PostResponseDto;

import com.example.inovaTest.services.FeedService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;



    @GetMapping("/{userId}")
    public ResponseEntity<List<PostResponseDto>> getFeed(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
    
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<PostResponseDto> feedPage = feedService.getUserFeed(userId, pageRequest);
        
        System.out.println("Returning " + feedPage.getContent().size() + " posts");
        
        return ResponseEntity.ok(feedPage.getContent());
    }

    
}
