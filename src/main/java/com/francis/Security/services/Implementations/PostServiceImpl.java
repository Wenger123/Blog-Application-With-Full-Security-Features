package com.francis.Security.services.Implementations;

import com.francis.Security.dtos.ResponseDto.PostPageResponseDto;
import com.francis.Security.dtos.ResponseDto.PostResponseDto;
import com.francis.Security.dtos.requestDto.PostDto;
import com.francis.Security.exceptions.PostNotFoundException;
import com.francis.Security.exceptions.UnauthorizedUserException;
import com.francis.Security.exceptions.UserNotFoundException;
import com.francis.Security.models.entities.Post;
import com.francis.Security.models.entities.UserEntity;
import com.francis.Security.models.errors.ErrorModel;
import com.francis.Security.repositories.PostRepository;
import com.francis.Security.repositories.UserRepository;
import com.francis.Security.services.PostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper = new ModelMapper();

    @Override
    public PostResponseDto createPost(PostDto postDto, Long userId) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            Post newPost = mapper.map(postDto, Post.class);

            newPost.setUserEntity(optionalUser.get());

            newPost = postRepository.save(newPost);

            return PostResponseDto.builder()
                    .postId(newPost.getPostId())
                    .createdDate(newPost.getCreatedDate())
                    .content(newPost.getContent())
                    .title(newPost.getTitle())
                    .userId(newPost.getUserEntity().getUserId())
                    .build();

        } else {
            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode("USER_ID_NOT_EXIST");
            errorModel.setMessage("User does not exist");
            errorModelList.add(errorModel);
            throw new UserNotFoundException(errorModelList);
        }
    }

    @Override
    public PostPageResponseDto getAllPostByUser( int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Post> postPage = postRepository.findAll( pageable);
        List<Post> postList = postPage.getContent();
        List<PostResponseDto> postDtoList = new ArrayList<>();
        for (Post post : postList) {
            postDtoList.add(mapper.map(post, PostResponseDto.class));
        }
        return PostPageResponseDto.builder()
                .content(postDtoList)
                .pageNo(postPage.getNumber())
                .pageSize(postPage.getSize())
                .totalElements(postPage.getTotalElements())
                .totalPages(postPage.getTotalPages())
                .lastPage(postPage.isLast())
                .build();
    }


    @Override
    public PostResponseDto getPostByUser(Long postId, Long userId) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            Post existingPost = post.get();
            if (existingPost.getUserEntity().getUserId().equals(userId)) {
                return mapper.map(existingPost, PostResponseDto.class);
            } else {
                throw new UnauthorizedUserException("You are not authorized to access this post." + userId);
            }
        } else {
            throw new PostNotFoundException("Post NOt Found with this postId " + postId);
        }
    }

    @Override
    public PostResponseDto updatePostByUser(PostDto postDto, Long postId, Long userId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();
            if (existingPost.getUserEntity().getUserId().equals(userId)) {
                existingPost.setTitle(postDto.getTitle());
                existingPost.setContent(postDto.getContent());

                Post updatedPost = postRepository.saveAndFlush(existingPost);
                return mapper.map(updatedPost, PostResponseDto.class);
            } else {
                throw new UnauthorizedUserException("You are not authorized to access this post." + userId);
            }
        } else {
            throw new PostNotFoundException("Post NOt Found with this postId" + postId);
        }
    }

    @Override
    public PostResponseDto updatePostTitleByUser(PostDto postDto, Long postId, Long userId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();
            if (existingPost.getUserEntity().getUserId().equals(userId)) {
                existingPost.setTitle(postDto.getTitle());
                Post updatedPost = postRepository.saveAndFlush(existingPost);
                return mapper.map(updatedPost, PostResponseDto.class);
            } else {
                throw new UnauthorizedUserException("You are not authorized to access this post." + userId);
            }
        } else {
            throw new PostNotFoundException("Post NOt Found with this postId" + postId);
        }
    }

    @Override
    public PostResponseDto updatePostContentByUser(PostDto postDto, Long postId, Long userId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();
            if (existingPost.getUserEntity().getUserId().equals(userId)) {
                existingPost.setContent(postDto.getContent());
                Post updatedPost = postRepository.saveAndFlush(existingPost);
                return mapper.map(updatedPost, PostResponseDto.class);
            } else {
                throw new UnauthorizedUserException("You are not authorized to access this post." + userId);
            }
        } else {
            throw new PostNotFoundException("Post NOt Found with this postId" + postId);
        }
    }

    @Override
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }
}
