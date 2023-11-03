package com.francis.Security.services;

import com.francis.Security.dtos.ResponseDto.UserResponseDto;
import com.francis.Security.dtos.ResponseDto.AuthResponseDTO;
import com.francis.Security.dtos.requestDto.LoginDto;
import com.francis.Security.dtos.requestDto.RegisterDto;
import com.francis.Security.dtos.requestDto.UserDto;

public interface UserService {
    UserResponseDto registerUser(RegisterDto registerDto);
    AuthResponseDTO loginUser(LoginDto loginDto);
    UserResponseDto updateUser(UserDto userDto,Long userId, String email);
    UserResponseDto updateUserName(UserDto userDto,Long userId, String email);
    UserResponseDto updateUserPhoneNumber(UserDto userDto,Long userId, String email);
    UserResponseDto updateUserPassword(UserDto userDto,Long userId, String email);
    UserResponseDto updateUserAddress(UserDto userDto, Long userId, String email);
    void deleteUser(Long userId);
    void logoutUser();
}
