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

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Objects;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public Page<PostModel> getFeed(UUID userId, Pageable pageable) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<UserModel> following = followRepository.findFollowedUsers(user);
        following.add(user); // incluir o próprio usuário no feed

        return postRepository.findByUserIn(following, pageable);
    }


    public PostResponseDto createPost(PostRequestDto dto) {
        UserModel user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Validação: apenas um tipo de mídia permitido
        if (dto.getImageUrl() != null && dto.getVideoUrl() != null) {
            throw new IllegalArgumentException("Só é permitido enviar imagem ou vídeo, não ambos.");
        }

        PostModel post = new PostModel();
        post.setUser(user);
        post.setContent(dto.getContent());
        post.setImageUrl(dto.getImageUrl());
        post.setVideoUrl(dto.getVideoUrl());

        PostModel savedPost = postRepository.save(post);

        // Converter para PostResponseDto
        PostResponseDto response = new PostResponseDto();
        response.setId(savedPost.getId());
        response.setUserId(user.getId());
        response.setUsername(user.getLogin());
        response.setContent(savedPost.getContent());
        response.setImageUrl(savedPost.getImageUrl());
        response.setVideoUrl(savedPost.getVideoUrl());
        response.setCreatedAt(savedPost.getCreatedAt());

        return response;
    }

    public Page<PostResponseDto> getPostsByUser(UUID userId, Pageable pageable) {
        var posts = postRepository.findByUserId(userId, pageable);
        return posts.map(this::convertToDto);
    }
    
    public List<PostResponseDto> getAllPostsByUser(UUID userId) {
    var posts = postRepository.findByUserIdOrderByCreatedAtDesc(userId);
    return posts.stream()
                .map(this::convertToDto)
                .toList();
}

    private PostResponseDto convertToDto(PostModel post) {
        PostResponseDto dto = new PostResponseDto();
        dto.setId(post.getId());
        dto.setUserId(post.getUser().getId());
        dto.setUsername(post.getUser().getUsername());
        dto.setContent(post.getContent());
        dto.setImageUrl(post.getImageUrl());
        dto.setVideoUrl(post.getVideoUrl());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setProfilePicture(post.getUser().getProfilePicture());
        return dto;
    }
    



    public Optional<String> deletePost(UUID postId, UUID userId) {
        PostModel post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post não foi encontrado"));

        if (!post.getUser().getId().equals(userId)) {
            throw new SecurityException("Usuário não autorizado para deletar o post");
        }

        // Prioriza imagem, depois vídeo, se nenhum, Optional.empty()
        String path = null;
        if (StringUtils.hasLength(post.getImageUrl())) {
            path = post.getImageUrl();
        } else if (StringUtils.hasLength(post.getVideoUrl())) {
            path = post.getVideoUrl();
        }

        postRepository.delete(post);

        return Optional.ofNullable(path);
    }

    public PostModel getPostModel(UUID postId){
        PostModel post = postRepository.getById(postId);
        return post;
    }


}
