package com.francis.Security.controllers;

import com.francis.Security.dtos.ResponseDto.UserResponseDto;
import com.francis.Security.dtos.ResponseDto.AuthResponseDTO;
import com.francis.Security.dtos.requestDto.LoginDto;
import com.francis.Security.dtos.requestDto.RegisterDto;
import com.francis.Security.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final UserService userService;

  @PostMapping(value ={ "/register","/signup"})
  public ResponseEntity<UserResponseDto> registerNewUser(@Valid @RequestBody RegisterDto registerDto){
      return ResponseEntity.ok(userService.registerUser(registerDto));
  }

  @PostMapping(value = {"/login","/sign-in"})
    public ResponseEntity<AuthResponseDTO> loginUser(@Valid @RequestBody LoginDto loginDto){
      return ResponseEntity.ok(userService.loginUser(loginDto));
  }
}


