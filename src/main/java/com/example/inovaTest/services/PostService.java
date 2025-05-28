package com.example.inovaTest.services;

import com.example.inovaTest.dtos.user.posts.PostRequestDto;
import com.example.inovaTest.dtos.user.posts.PostResponseDto;
import com.example.inovaTest.exceptions.ResourceNotFoundException;
import com.example.inovaTest.models.PostModel;
import com.example.inovaTest.models.UserModel;
import com.example.inovaTest.repositories.FollowRepository;
import com.example.inovaTest.repositories.PostRepository;
import com.example.inovaTest.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final LikeService likeService;



    public PostResponseDto createPost(PostRequestDto dto) {
        UserModel user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (dto.getImageUrl() != null && dto.getVideoUrl() != null) {
            throw new IllegalArgumentException("Só é permitido enviar imagem ou vídeo, não ambos.");
        }

        PostModel post = new PostModel();
        post.setUser(user);
        post.setContent(dto.getContent());
        post.setImageUrl(dto.getImageUrl());
        post.setVideoUrl(dto.getVideoUrl());

        PostModel savedPost = postRepository.save(post);

        return convertToDto(savedPost, dto.getUserId());
    }

    public Page<PostResponseDto> getPostsByUser(UUID userId, Pageable pageable) {
        var posts = postRepository.findByUserId(userId, pageable);
        return posts.map(post -> convertToDto(post, userId));
    }

    public List<PostResponseDto> getAllPostsByUser(UUID userId) {
        var posts = postRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return posts.stream()
                .map(post -> convertToDto(post, userId))
                .toList();
    }

    public Optional<String> deletePost(UUID postId, UUID userId) {
        PostModel post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post não foi encontrado"));

        if (!post.getUser().getId().equals(userId)) {
            throw new SecurityException("Usuário não autorizado para deletar o post");
        }

        String path = null;
        if (StringUtils.hasLength(post.getImageUrl())) {
            path = post.getImageUrl();
        } else if (StringUtils.hasLength(post.getVideoUrl())) {
            path = post.getVideoUrl();
        }

        postRepository.delete(post);
        return Optional.ofNullable(path);
    }

    public PostModel getPostModel(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post não encontrado"));
    }

    public PostResponseDto getPostResponseDto(UUID postId, UUID userId) {
        PostModel post = getPostModel(postId);
        return convertToDto(post, userId);
    }

    private PostResponseDto convertToDto(PostModel post, UUID userId) {
        PostResponseDto dto = new PostResponseDto();
        dto.setId(post.getId());
        dto.setUserId(post.getUser().getId());
        dto.setUsername(post.getUser().getUsername());
        dto.setContent(post.getContent());
        dto.setImageUrl(post.getImageUrl());
        dto.setVideoUrl(post.getVideoUrl());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setProfilePicture(post.getUser().getProfilePicture());
        dto.setLikesCount(likeService.countPostLikes(post.getId()));
        dto.setLiked(likeService.hasUserLikedPost(post.getId(), userId));
        dto.setCommentsCount(post.getComments().size());
        dto.setLiked(likeService.hasUserLikedPost(post.getId(), userId));
        System.out.println("cheguei aqui: "+dto.isLiked());
        return dto;

    }
}
