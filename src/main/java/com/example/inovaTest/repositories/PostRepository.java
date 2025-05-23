package com.example.inovaTest.repositories;

import com.example.inovaTest.models.PostModel;
import com.example.inovaTest.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface PostRepository extends JpaRepository<PostModel, UUID> {
    Page<PostModel> findByUserIn(List<UserModel> users, Pageable pageable);
    Page<PostModel> findByUserId(UUID userId, Pageable pageable);
    List<PostModel> findByUserIdOrderByCreatedAtDesc(UUID userId);
    List<PostModel> findByUserInOrderByCreatedAtDesc(List<UserModel> users);


}
