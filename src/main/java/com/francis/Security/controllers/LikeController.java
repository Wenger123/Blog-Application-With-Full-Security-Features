package com.francis.Security.controllers;

import com.francis.Security.dtos.ResponseDto.LikeResponseDto;
import com.francis.Security.services.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/like/{userId}/{postId}")
    public ResponseEntity<?> likeOrUnlikePost(@PathVariable("userId") Long userId, @PathVariable("postId") Long postId) {
        try {
            LikeResponseDto responseDto = likeService.likeAndUnLikePost(userId, postId);
            return ResponseEntity.ok(Objects.requireNonNullElse(responseDto, "Post unliked successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to like or unlike post");
        }
    }

    @GetMapping("/likes/{userId}/{postId}")
    public ResponseEntity<List<LikeResponseDto>> getAllLikeByUser(@PathVariable("userId") Long userId,
                                                                  @PathVariable("postId") Long postId) {
        return ResponseEntity.ok(likeService.getAllLikesByUser(userId, postId));
    }
}

