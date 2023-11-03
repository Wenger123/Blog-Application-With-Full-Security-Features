package com.francis.Security.repositories;

import com.francis.Security.models.entities.Like;
import com.francis.Security.models.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like,Long> {
    Like findByPostEntityPostIdAndUserEntityUserId(Long postId, Long userId);

    List<Like> findAllByPostEntity(Post post);
}
