package com.example.inovaTest.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.inovaTest.models.FollowModel;
import com.example.inovaTest.models.UserModel;

public interface FollowRepository extends JpaRepository<FollowModel, UUID> {
    @Query("SELECT f.followed FROM FollowModel f WHERE f.follower = :user")
    List<UserModel> findFollowedUsers(@Param("user") UserModel user);
}
