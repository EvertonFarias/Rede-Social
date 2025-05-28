package com.example.inovaTest.repositories;

import com.example.inovaTest.models.LikeModel;
import com.example.inovaTest.models.PostModel;
import com.example.inovaTest.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<LikeModel, UUID> {
    Optional<LikeModel> findByPostIdAndUserId(UUID postId, UUID userId);
    boolean existsByPostIdAndUserId(UUID postId, UUID userId);
    long countByPostId(UUID postId);

    Optional<LikeModel> findByCommentIdAndUserId(UUID commentId, UUID userId);
    boolean existsByCommentIdAndUserId(UUID commentId, UUID userId);
    long countByCommentId(UUID commentId);
}

