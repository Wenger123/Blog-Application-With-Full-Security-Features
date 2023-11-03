package com.francis.Security.services;
import com.francis.Security.dtos.ResponseDto.LikeResponseDto;
import java.util.List;

public interface LikeService {
    LikeResponseDto likeAndUnLikePost(Long userId, Long postId);
    List<LikeResponseDto> getAllLikesByUser(Long userId, Long postId);
}
