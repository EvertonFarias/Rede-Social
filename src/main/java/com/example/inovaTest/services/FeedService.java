package com.example.inovaTest.services;

import com.example.inovaTest.dtos.user.posts.PostResponseDto;
import com.example.inovaTest.enums.FriendshipStatus;
import com.example.inovaTest.models.FriendshipModel;
import com.example.inovaTest.models.PostModel;
import com.example.inovaTest.models.UserModel;
import com.example.inovaTest.repositories.FriendshipRepository;
import com.example.inovaTest.repositories.PostRepository;
import com.example.inovaTest.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final PostRepository postRepository;
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;
    private final PostService postService;

    public Page<PostResponseDto> getUserFeed(UUID userId, Pageable pageable) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<FriendshipModel> friendships = friendshipRepository.findBySenderOrReceiverAndStatus(user, user, FriendshipStatus.ACCEPTED);
        List<UserModel> friends = user.getFriends(friendships);

        List<UserModel> authors = new ArrayList<>(friends);
        authors.add(user);

        Page<PostModel> posts = postRepository.findByUserIn(authors, pageable);

        return posts.map(post -> postService.getPostResponseDto(post.getId(), userId));
    }
}
