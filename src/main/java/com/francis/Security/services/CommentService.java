package com.francis.Security.services;

import com.francis.Security.dtos.ResponseDto.CommentResponseDto;
import com.francis.Security.dtos.requestDto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentResponseDto createComment(Long userId, Long postId, CommentDto commentDto);
    List<CommentResponseDto> getAllComments(Long userId, Long postId);
    CommentResponseDto getCommentById( Long userId, Long postId, Long commentId);
    CommentResponseDto updateComment(CommentDto commentDto,Long commentId,Long postId, Long userId);
    void  deleteComment(Long commentId, Long postId, Long userId);
}
