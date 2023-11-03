package com.francis.Security.services.Implementations;

import com.francis.Security.dtos.ResponseDto.LikeResponseDto;
import com.francis.Security.exceptions.PostNotFoundException;
import com.francis.Security.exceptions.UnauthorizedUserException;
import com.francis.Security.models.entities.Like;
import com.francis.Security.models.entities.Post;
import com.francis.Security.models.entities.UserEntity;
import com.francis.Security.repositories.LikeRepository;
import com.francis.Security.repositories.PostRepository;
import com.francis.Security.repositories.UserRepository;
import com.francis.Security.services.LikeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper = new ModelMapper();

    @Override
    public LikeResponseDto likeAndUnLikePost(Long userId, Long postId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId " + userId));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with postId " + postId));
        Like existingLike = likeRepository.findByPostEntityPostIdAndUserEntityUserId(postId, userId);
        if (existingLike != null) {
           likeRepository.delete(existingLike);
           return null;
        }else {
            existingLike = Like.builder()
                    .userEntity(user)
                    .postEntity(post)
                    .build();
            likeRepository.save(existingLike);

            return mapper.map(existingLike, LikeResponseDto.class);
        }
    }

    @Override
    public List<LikeResponseDto> getAllLikesByUser(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with the postId " + postId));
        if (!post.getUserEntity().getUserId().equals(userId)) {
            throw new UnauthorizedUserException("You are not authorized to access Likes for this post.");
        }
        List<Like> likes = likeRepository.findAllByPostEntity(post);
        List<LikeResponseDto> likeResponseDtoList = new ArrayList<>();
        for (Like like : likes) {
            LikeResponseDto likeResponseDto = LikeResponseDto.builder()
                    .likeId(like.getLikeId())
                    .postId(like.getPostEntity().getPostId())
                    .userId(like.getUserEntity().getUserId())
                    .build();
            likeResponseDtoList.add(likeResponseDto);
        }
        return likeResponseDtoList;
    }
}
