package com.example.inovaTest.repositories;

import com.example.inovaTest.models.LikeModel;
import com.example.inovaTest.models.PostModel;
import com.example.inovaTest.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<LikeModel, UUID> {
    Optional<LikeModel> findByPostAndUser(PostModel post, UserModel user);
    long countByPost(PostModel post);
}
