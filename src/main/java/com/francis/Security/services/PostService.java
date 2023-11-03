package com.francis.Security.services;
import com.francis.Security.dtos.ResponseDto.PostPageResponseDto;
import com.francis.Security.dtos.ResponseDto.PostResponseDto;
import com.francis.Security.dtos.requestDto.PostDto;
public interface PostService {
    PostResponseDto createPost (PostDto postDto, Long userId);
    PostPageResponseDto getAllPostByUser( int pageNO, int pageSize, String sortBy, String sortDir);
    PostResponseDto getPostByUser(Long postId, Long userId);
    PostResponseDto updatePostByUser(PostDto postDto, Long postId, Long userId);
    PostResponseDto updatePostTitleByUser(PostDto postDto, Long postId, Long userId);
    PostResponseDto updatePostContentByUser(PostDto postDto, Long postId, Long userId);
    void deletePost(Long postId);
}
