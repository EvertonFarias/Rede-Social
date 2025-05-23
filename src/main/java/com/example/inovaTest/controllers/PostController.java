package com.example.inovaTest.controllers;


import com.example.inovaTest.dtos.user.comments.CommentRequestDto;
import com.example.inovaTest.dtos.user.comments.CommentResponseDto;
import com.example.inovaTest.dtos.user.posts.PostRequestDto;
import com.example.inovaTest.dtos.user.posts.PostResponseDto;
import com.example.inovaTest.models.CommentModel;
import com.example.inovaTest.models.PostModel;
import com.example.inovaTest.services.CommentService;
import com.example.inovaTest.services.FirebaseStorageService;
import com.example.inovaTest.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final FirebaseStorageService firebaseStorageService;


    
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> createPost(@Valid @RequestBody PostRequestDto dto) {
        PostResponseDto newPost = postService.createPost(dto);
        return ResponseEntity.ok(newPost);
    }

    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<List<PostResponseDto>> getUserPosts(@PathVariable UUID userId) {
        List<PostResponseDto> posts = postService.getAllPostsByUser(userId);
        return ResponseEntity.ok(posts);
    }


 @DeleteMapping("/posts/{postId}/users/{userId}")
    public ResponseEntity<Void> deletePost(
        @PathVariable UUID postId,
        @PathVariable UUID userId) {

        Optional<String> mediaPath = postService.deletePost(postId, userId);

        if (mediaPath != null && !mediaPath.isEmpty()) {
            try {
                firebaseStorageService.deleteFile(mediaPath.get());
                System.out.println("arquivo deletado do firebase");
            } catch (Exception e) {
        
                System.err.println("Erro ao deletar arquivo no Firebase: " + e.getMessage());
            }
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable UUID postId,
            @RequestBody @Valid CommentRequestDto commentRequestDto) {
        
        CommentResponseDto responseDto = commentService.createCommentDto(postId, commentRequestDto);
        return ResponseEntity.ok(responseDto);
    }


    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<?> getComments(@PathVariable UUID postId) {
        PostModel post = postService.getPostModel(postId);
        List<CommentResponseDto> response = commentService.getCommentResponsesForPost(post);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity deleteComment(
            @PathVariable UUID postId,
            @PathVariable UUID commentId) {
        commentService.deleteComment(postId, commentId);
        return ResponseEntity.ok().build();
    }

}
