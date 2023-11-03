package com.francis.Security.dtos.requestDto;

import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @NotNull(message = "can't be null")
    @NotBlank(message = "userName required")
    @NotEmpty(message = "Must not be empty")
    @Size(min = 3, max = 25, message = "letters between 3 and 25")
    private String usernameOrEmail;
    @NotNull(message = "can't be null")
    @NotBlank(message = "password required")
    @NotEmpty(message = "Must not be empty")
    @Size(min = 8, max = 25, message = "Must be between 8 and 25")
    private String password;

}
