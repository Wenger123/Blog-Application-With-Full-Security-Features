package com.francis.Security.dtos.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    @NotNull(message = "can't be null")
    @NotBlank(message = "firstName required")
    @NotEmpty(message = "Must not be empty")
    @Size(min = 3, max = 15, message = "letters between 3 and 15")
    private String firstName;
    @NotNull(message = "can't be null")
    @NotBlank(message = "lastName required")
    @NotEmpty(message = "Must not be empty")
    @Size(min = 3, max = 15, message = "letters between 3 and 15")
    private String lastName;
    @NotNull(message = "can't be null")
    @NotBlank(message = "userName required")
    @NotEmpty(message = "Must not be empty")
    @Size(min = 3, max = 15, message = "letters between 3 and 15")
    private String username;
    @NotNull(message = "can't be null")
    @NotBlank(message = "email required")
    @NotEmpty(message = "Must not be empty")
    @Size(min = 8, max = 30, message = "letters between 8 and 30")
    @Email(message = "Enter a valid email")
    @Column(unique = true)
    private String email;
    @NotNull(message = "can't be null")
    @NotBlank(message = "password required")
    @NotEmpty(message = "Must not be empty")
    @Size(min = 8, max = 25, message = "Must be between 8 and 25")
    private String password;
    @NotNull(message = "can't be null")
    @NotBlank(message = "confirmedPassword required")
    @NotEmpty(message = "Must not be empty")
    @Size(min = 8, max = 25, message = "Must be between 8 and 25")
    @Transient
    private String confirmPassword;
    @NotNull(message = "can't be null")
    @NotBlank(message = "phoneNumber required")
    @NotEmpty(message = "Must not be empty")
    @Digits(integer = 11, fraction = 0, message = "Please enter a valid numeric value")
    @Size(min = 11,max = 11,message = "must be 11 numbers")
    private String phoneNumber;
    @NotNull(message = "can't be null")
    @NotBlank(message = "address required")
    @NotEmpty(message = "Must not be empty")
    @Size(min = 3, max = 25, message = "Must be between 3 and 25")
    private String address;

}
