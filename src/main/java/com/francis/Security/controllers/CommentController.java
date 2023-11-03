package com.francis.Security.controllers;

import com.francis.Security.dtos.ResponseDto.CommentResponseDto;
import com.francis.Security.dtos.requestDto.CommentDto;
import com.francis.Security.services.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user/{userId}/post/{postId}")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/create-comment")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable("userId")Long userId,
                                                            @PathVariable("postId")Long postId,
                                                            @Valid @RequestBody CommentDto commentDto){
        return ResponseEntity.ok(commentService.createComment(userId, postId, commentDto));
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentResponseDto>> getAllComments(@PathVariable("userId")Long userId,
                                                                   @PathVariable("postId")Long postId){
        return ResponseEntity.ok(commentService.getAllComments(userId,postId));
    }

    @GetMapping("/comment/{commentId}")
    public ResponseEntity<CommentResponseDto> getCommentByUser(@PathVariable("userId")Long userId,
                                                               @PathVariable("postId")Long postId,
                                                               @PathVariable("commentId")Long commentId){
        return ResponseEntity.ok(commentService.getCommentById(userId, postId, commentId));
    }

    @PutMapping("/update-comment/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable("userId")Long userId,
                                                            @PathVariable("postId")Long postId,
                                                            @PathVariable("commentId")Long commentId,
                                                            @Valid @RequestBody CommentDto commentDto){
        return ResponseEntity.ok(commentService.updateComment(commentDto, commentId, postId, userId));
    }

    @DeleteMapping("/delete-comment/{commentId}")
    public ResponseEntity<String> removeComment(@PathVariable("userId")Long userId,
                                                @PathVariable("postId")Long postId,
                                                @PathVariable("commentId")Long commentId){
        try {
            commentService.deleteComment(userId, postId, commentId);
            return ResponseEntity.ok("Comment with ID " + commentId + " deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete comment with ID " + commentId);
        }
    }
}
