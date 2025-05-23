package com.example.inovaTest.services;

import com.example.inovaTest.dtos.user.posts.PostResponseDto;
import com.example.inovaTest.models.FriendshipModel;
import com.example.inovaTest.models.PostModel;
import com.example.inovaTest.models.UserModel;
import com.example.inovaTest.enums.FriendshipStatus;
import com.example.inovaTest.repositories.FriendshipRepository;
import com.example.inovaTest.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final PostRepository postRepository;
    private final FriendshipRepository friendshipRepository;

    public List<PostResponseDto> getUserFeed(UserModel user) {
        List<FriendshipModel> friendships = friendshipRepository.findBySenderOrReceiverAndStatus(user, user, FriendshipStatus.ACCEPTED);
        List<UserModel> friends = user.getFriends(friendships);
        List<UserModel> authors = new ArrayList<>(friends);
        authors.add(user);
        List<PostModel> posts = postRepository.findByUserInOrderByCreatedAtDesc(authors);
        return posts.stream().map(this::toDto).collect(Collectors.toList());
    }

    private PostResponseDto toDto(PostModel post) {
        PostResponseDto dto = new PostResponseDto();
        dto.setId(post.getId());
        dto.setUserId(post.getUser().getId());
        dto.setUsername(post.getUser().getLogin()); 
        dto.setContent(post.getContent());
        dto.setImageUrl(post.getImageUrl());
        dto.setVideoUrl(post.getVideoUrl());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setProfilePicture(post.getUser().getProfilePicture());
        return dto;
    }
}
