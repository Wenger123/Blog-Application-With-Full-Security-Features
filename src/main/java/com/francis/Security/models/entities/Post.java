package com.francis.Security.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "Post_Table")
public class Post {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long postId;

    private String title;

    private String content;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "postEntity",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> commentEntity;

    @OneToMany(mappedBy = "postEntity",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Like> likeEntity;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @Transient
    private Long commentCount;

    @Transient
    private Long likeCount;
}
