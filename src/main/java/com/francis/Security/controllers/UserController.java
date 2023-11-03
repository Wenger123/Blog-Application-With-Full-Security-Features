package com.francis.Security.controllers;

import com.francis.Security.dtos.ResponseDto.UserResponseDto;
import com.francis.Security.dtos.requestDto.UserDto;
import com.francis.Security.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class UserController {
    private final UserService userService;


    @PutMapping("/user/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable("userId") Long userId,
                                                      @RequestParam String email,
                                                      @Valid @RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.updateUser(userDto,userId,email));
    }
    @PatchMapping("/user/{userId}")
    public ResponseEntity<UserResponseDto> updateUserName(@PathVariable("userId")Long userId,
                                                          @RequestParam String email,
                                                          @Valid @RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.updateUserName(userDto,userId,email));
    }
    @PatchMapping("/user1/{userId}")
    public ResponseEntity<UserResponseDto> updateUserPhoneNumber(@PathVariable("userId")Long userId,
                                                                 @RequestParam String email,
                                                                 @Valid @RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.updateUserPhoneNumber(userDto,userId,email));
    }
    @PatchMapping("/user2/{userId}")
    public ResponseEntity<UserResponseDto> updateUserPassword(@PathVariable("userId")Long userId,
                                                              @RequestParam String email,
                                                              @Valid @RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.updateUserPassword(userDto,userId,email));
    }
    @PatchMapping("/user3/{userId}")
    public ResponseEntity<UserResponseDto> updateUserAddress(@PathVariable("userId")Long userId,
                                                             @RequestParam String email,
                                                             @Valid @RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.updateUserAddress(userDto,userId,email));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> removeUser(@PathVariable("userId") Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("User with ID " + userId + " deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user with ID because it is not found " + userId);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser() {
        userService.logoutUser();
        return ResponseEntity.ok("User logged out successfully.");
    }
}
