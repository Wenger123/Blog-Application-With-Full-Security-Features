package com.francis.Security.dtos.ResponseDto;

import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LikeResponseDto {
    private Long likeId;
    private Long postId;
    private Long userId;
}

