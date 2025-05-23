package com.example.inovaTest.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.inovaTest.models.LikeModel;
import com.example.inovaTest.models.PostModel;
import com.example.inovaTest.models.UserModel;
import com.example.inovaTest.repositories.LikeRepository;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    public void toggleLike(PostModel post, UserModel user) {
        Optional<LikeModel> existingLike = likeRepository.findByPostAndUser(post, user);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
        } else {
            LikeModel like = new LikeModel();
            like.setPost(post);
            like.setUser(user);
            likeRepository.save(like);
        }
    }

    public long countLikes(PostModel post) {
        return likeRepository.countByPost(post);
    }
}
