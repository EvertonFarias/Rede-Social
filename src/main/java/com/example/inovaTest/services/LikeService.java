package com.example.inovaTest.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.inovaTest.models.LikeModel;
import com.example.inovaTest.repositories.CommentRepository;
import com.example.inovaTest.repositories.LikeRepository;
import com.example.inovaTest.repositories.PostRepository;
import com.example.inovaTest.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public boolean togglePostLike(UUID postId, UUID userId) {
        Optional<LikeModel> existing = likeRepository.findByPostIdAndUserId(postId, userId);
        if (existing.isPresent()) {
            likeRepository.delete(existing.get());
            return false; // Like was removed
        } else {
            LikeModel like = new LikeModel();
            like.setPost(postRepository.getReferenceById(postId));
            like.setUser(userRepository.getReferenceById(userId));
            likeRepository.save(like);
            return true; // Like was added
        }
    }

    public boolean hasUserLikedPost(UUID postId, UUID userId) {
        return likeRepository.existsByPostIdAndUserId(postId, userId);
    }

    public long countPostLikes(UUID postId) {
        return likeRepository.countByPostId(postId);
    }
}
