package com.example.inovaTest.repositories;

import com.example.inovaTest.models.CommentModel;
import com.example.inovaTest.models.PostModel;
import com.google.common.base.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<CommentModel, UUID> {
    List<CommentModel> findByPost(PostModel post);
     Optional<CommentModel> findByIdAndPostId(UUID commentId, UUID postId);
    
}
