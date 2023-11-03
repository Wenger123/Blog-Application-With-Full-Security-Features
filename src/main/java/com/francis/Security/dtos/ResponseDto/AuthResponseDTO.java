package com.francis.Security.dtos.ResponseDto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponseDTO  {
    private String accessToken;
    private String tokenType;
    private String userType;
}
