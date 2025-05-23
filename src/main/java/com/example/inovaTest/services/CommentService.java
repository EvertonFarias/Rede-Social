package com.example.inovaTest.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.inovaTest.dtos.user.comments.CommentRequestDto;
import com.example.inovaTest.dtos.user.comments.CommentResponseDto;
import com.example.inovaTest.models.CommentModel;
import com.example.inovaTest.models.PostModel;
import com.example.inovaTest.models.UserModel;
import com.example.inovaTest.repositories.CommentRepository;
import com.example.inovaTest.repositories.PostRepository;
import com.example.inovaTest.repositories.UserRepository;
import com.google.common.base.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public CommentResponseDto createCommentDto(UUID postId, CommentRequestDto commentRequestDto) {
        PostModel post = postRepository.getById(postId);
        UserModel user = userRepository.getById(commentRequestDto.getUserId());

        CommentModel comment = new CommentModel();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(commentRequestDto.getContent());

        CommentModel savedComment = commentRepository.save(comment);

        return new CommentResponseDto(
            savedComment.getId(),
            savedComment.getContent(),
            savedComment.getCreatedAt(),
            savedComment.getUser().getProfilePicture(),
            savedComment.getUser().getUsername(),
            comment.getUser().getId()
        );
    }


    public List<CommentResponseDto> getCommentResponsesForPost(PostModel post) {
        List<CommentModel> comments = getCommentForPost(post);
        return comments.stream()
            .map(comment -> new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUser().getProfilePicture(),
                comment.getUser().getUsername(),
                comment.getUser().getId()

            ))
            .toList();
    }


    public List<CommentModel> getCommentForPost(PostModel post) {
        return commentRepository.findByPost(post);
    }

    public void deleteComment(UUID postId, UUID commentId){
        System.out.println("Comentário de UUID:"+commentId+" deletado");
        Optional<CommentModel> comment = commentRepository.findByIdAndPostId(commentId, postId);
        commentRepository.delete(comment.get());
    }

}

