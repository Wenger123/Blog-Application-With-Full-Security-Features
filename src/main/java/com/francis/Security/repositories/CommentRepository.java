package com.francis.Security.repositories;

import com.francis.Security.models.entities.Comment;
import com.francis.Security.models.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findAllByPostEntity(Post post);
}
