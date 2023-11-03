package com.francis.Security.services.Implementations;

import com.francis.Security.dtos.ResponseDto.CommentResponseDto;
import com.francis.Security.dtos.requestDto.CommentDto;
import com.francis.Security.exceptions.CommentNotFoundException;
import com.francis.Security.exceptions.PostNotFoundException;
import com.francis.Security.exceptions.UnauthorizedUserException;
import com.francis.Security.models.entities.Comment;
import com.francis.Security.models.entities.Post;
import com.francis.Security.models.entities.UserEntity;
import com.francis.Security.repositories.CommentRepository;
import com.francis.Security.repositories.PostRepository;
import com.francis.Security.repositories.UserRepository;
import com.francis.Security.services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public CommentResponseDto createComment(Long userId, Long postId, CommentDto commentDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not Found with id, " + postId));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId " + userId));
        Comment newComment = Comment.builder()
                .postEntity(post)
                .userEntity(user)
                .comment(commentDto.getComment())
                .build();

        newComment = commentRepository.save(newComment);

        return CommentResponseDto.builder()
                .commentId(newComment.getCommentId())
                .comment(newComment.getComment())
                .commentDate(newComment.getCommentDate())
                .postId(newComment.getPostEntity().getPostId())
                .userId(newComment.getUserEntity().getUserId())
                .build();
    }
    @Override
    public List<CommentResponseDto> getAllComments(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new PostNotFoundException("Post not Found with id, " + postId));
        if (!post.getUserEntity().getUserId().equals(userId)){
            throw new UnauthorizedUserException("You are not authorized to access comments for this post.");
        }
        List<Comment> comments = commentRepository.findAllByPostEntity(post);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : comments){
            CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                    .commentId(comment.getCommentId())
                    .comment(comment.getComment())
                    .commentDate(comment.getCommentDate())
                    .postId(comment.getPostEntity().getPostId())
                    .userId(comment.getUserEntity().getUserId())
                    .build();
            commentResponseDtoList.add(commentResponseDto);
        }
        return commentResponseDtoList;
    }

    @Override
    public CommentResponseDto getCommentById(Long userId,Long postId, Long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new PostNotFoundException("Post not Found with id, " + postId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new CommentNotFoundException("Comment not found with commentId " + commentId));

        if(!post.getUserEntity().getUserId().equals(userId) && !comment.getUserEntity().getUserId().equals(userId)){
            throw  new UnauthorizedUserException("You are not authorized to access comments for this post.");
        }
        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .comment(comment.getComment())
                .commentDate(comment.getCommentDate())
                .postId(comment.getPostEntity().getPostId())
                .userId(comment.getUserEntity().getUserId())
                .build();
    }

    @Override
    public CommentResponseDto updateComment(CommentDto commentDto, Long commentId, Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new PostNotFoundException("Post not Found with id, " + postId));
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if(optionalComment.isPresent()){
            Comment existingComment = optionalComment.get();
            if(post.getPostId().equals(postId)&& existingComment.getCommentId().equals(commentId)){
                Comment updatedComment = commentRepository.saveAndFlush(existingComment);
                return mapper.map(updatedComment,CommentResponseDto.class);
            }else {
                throw new UnauthorizedUserException("You are not authorized to access this comment ");
            }
        }else {
            throw new CommentNotFoundException("comment not found with commentId "+commentId);
        }
    }

    @Override
    public void deleteComment(Long commentId,Long postId, Long userId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if(optionalComment.isPresent()){
            Comment existingComment = optionalComment.get();
            if(!existingComment.getPostEntity().getPostId().equals(postId)&& !existingComment.getUserEntity().getUserId().equals(userId)){
                throw  new UnauthorizedUserException("You are not authorized to delete this comment ");
            }else {
                commentRepository.deleteById(commentId);
            }
        }else {
            throw new CommentNotFoundException("comment not found with commentId "+commentId);
        }

    }
}
