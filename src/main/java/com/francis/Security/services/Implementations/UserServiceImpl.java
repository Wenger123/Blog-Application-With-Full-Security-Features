package com.francis.Security.services.Implementations;

import com.francis.Security.dtos.ResponseDto.UserResponseDto;
import com.francis.Security.dtos.ResponseDto.AuthResponseDTO;
import com.francis.Security.dtos.requestDto.LoginDto;
import com.francis.Security.dtos.requestDto.RegisterDto;
import com.francis.Security.dtos.requestDto.UserDto;
import com.francis.Security.exceptions.*;
import com.francis.Security.models.entities.Role;
import com.francis.Security.models.entities.UserEntity;
import com.francis.Security.repositories.RoleRepository;
import com.francis.Security.repositories.UserRepository;
import com.francis.Security.security.JwtGenerator;
import com.francis.Security.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtGenerator jwtGenerator;
    private final ModelMapper mapper = new ModelMapper();
    @Override
    public UserResponseDto registerUser(RegisterDto registerDto) {

    if (userRepository.count()==0){
        UserEntity user = UserEntity.builder()
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .confirmPassword(registerDto.getConfirmPassword())
                .phoneNumber(registerDto.getPhoneNumber())
                .address(registerDto.getAddress())

                .build();
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RoleNotFoundException("Role not found with name ADMIN"));
            user.setRoles(Collections.singletonList(adminRole));
            UserEntity savedUser = userRepository.save(user);
            UserResponseDto userResponseDto = mapper.map(savedUser,UserResponseDto.class);
            userResponseDto.setAdmin(true);
            return userResponseDto;
        } else {
        if (userRepository.existsByUsername(registerDto.getUsername()) ||
                userRepository.existsByEmail(registerDto.getEmail())) {
            throw new UsernameAlreadyExistException("userName not available");

        }
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new UsernameAlreadyExistException("Email is already exists!.");
        }
        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            throw new PasswordMissMatchException("password mismatch try again ");
        }

        UserEntity user = UserEntity.builder()
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .confirmPassword(registerDto.getConfirmPassword())
                .phoneNumber(registerDto.getPhoneNumber())
                .address(registerDto.getAddress())

                .build();
        Role adminRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RoleNotFoundException("Role not found with name USER"));
        user.setRoles(Collections.singletonList(adminRole));
        UserEntity savedUser = userRepository.save(user);
        UserResponseDto userResponseDto = mapper.map(savedUser,UserResponseDto.class);
        userResponseDto.setAdmin(false);
        return userResponseDto;
    }

    }

    @Override
    public AuthResponseDTO loginUser(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            String token = jwtGenerator.generateToken(authentication);

            String userType = getUserType(authentication);

            AuthResponseDTO authResponseDTO = AuthResponseDTO.builder()
                    .accessToken(token)
                    .userType(userType)
                    .tokenType("Bearer ")
                    .build();

            return mapper.map(authResponseDTO, AuthResponseDTO.class);
        }catch (BadCredentialsException e){
            throw new IncorrectCredentialsException("Incorrect username or password");
        }
    }
    private String getUserType(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ADMIN")) {
                return "ADMIN";
            }
        }
        return "USER";
    }
    @Override
    public UserResponseDto updateUser(UserDto userDto, Long userId, String email) {
        Optional<UserEntity> optionalUser = userRepository.findByUsernameOrEmail(userDto.getUsername(),userDto.getEmail());
        if (optionalUser.isPresent()){
            UserEntity existingUser = optionalUser.get();
            if(existingUser.getUserId().equals(userId)&&existingUser.getEmail().equals(email)){
                existingUser.setFirstName(userDto.getFirstName());
                existingUser.setLastName(userDto.getLastName());
                existingUser.setUsername(userDto.getUsername());
                existingUser.setPhoneNumber(userDto.getPhoneNumber());
                existingUser.setPassword(userDto.getPassword());
                existingUser.setAddress(userDto.getAddress());
                UserEntity updatedUser = userRepository.saveAndFlush(existingUser);
                return mapper.map(updatedUser,UserResponseDto.class);
            }else {
                throw new UnauthorizedUserException("You are not authorized to access this "+ userId);
            }
        }else {
            throw new UsernameNotFoundException( "User not found with this userId "+userId);
        }
    }

    @Override
    public UserResponseDto updateUserName(UserDto userDto, Long userId, String email) {
        Optional<UserEntity> optionalUser = userRepository.findByUsernameOrEmail(userDto.getUsername(),userDto.getEmail());
        if(optionalUser.isPresent()){
            UserEntity existingUser = optionalUser.get();
            if (existingUser.getUserId().equals(userId)&& existingUser.getEmail().equals(email)){
                existingUser.setFirstName(userDto.getFirstName());
                existingUser.setLastName(userDto.getLastName());
                UserEntity updatedUser = userRepository.saveAndFlush(existingUser);
                return mapper.map(updatedUser,UserResponseDto.class);
            }else {
                throw new UnauthorizedUserException("You are not authorized to access this "+ userId);
            }
        }else {
            throw new UsernameNotFoundException("User not found with this userId "+userId);
        }
    }

    @Override
    public UserResponseDto updateUserPhoneNumber(UserDto userDto, Long userId, String email) {
        Optional<UserEntity> optionalUser = userRepository.findByUsernameOrEmail(userDto.getUsername(),userDto.getEmail());
        if(optionalUser.isPresent()){
            UserEntity existingUser = optionalUser.get();
            if (existingUser.getUserId().equals(userId)&& existingUser.getEmail().equals(email)){
                existingUser.setPhoneNumber(userDto.getPhoneNumber());
                UserEntity updatedUser = userRepository.saveAndFlush(existingUser);
                return mapper.map(updatedUser,UserResponseDto.class);
            }else {
                throw new UnauthorizedUserException("You are not authorized to access this "+ userId);
            }
        }else {
            throw new UsernameNotFoundException("User not found with this userId "+userId);
        }
    }

    @Override
    public UserResponseDto updateUserPassword(UserDto userDto, Long userId, String email) {
        Optional<UserEntity> optionalUser = userRepository.findByUsernameOrEmail(userDto.getUsername(),userDto.getEmail());
        if(optionalUser.isPresent()){
            UserEntity existingUser = optionalUser.get();
            if (existingUser.getUserId().equals(userId)&& existingUser.getEmail().equals(email)){
                existingUser.setPassword(userDto.getPassword());
                UserEntity updatedUser = userRepository.saveAndFlush(existingUser);
                return mapper.map(updatedUser,UserResponseDto.class);
            }else {
                throw new UnauthorizedUserException("You are not authorized to access this "+ userId);
            }
        }else {
            throw new UsernameNotFoundException("User not found with this userId "+userId);
        }
    }

    @Override
    public UserResponseDto updateUserAddress(UserDto userDto, Long userId, String email) {
        Optional<UserEntity> optionalUser = userRepository.findByUsernameOrEmail(userDto.getUsername(),userDto.getEmail());
        if(optionalUser.isPresent()){
            UserEntity existingUser = optionalUser.get();
            if (existingUser.getUserId().equals(userId)&& existingUser.getEmail().equals(email)){
                existingUser.setAddress(userDto.getAddress());
                UserEntity updatedUser = userRepository.saveAndFlush(existingUser);
                return mapper.map(updatedUser,UserResponseDto.class);
            }else {
                throw new UnauthorizedUserException("You are not authorized to access this "+ userId);
            }
        }else {
            throw new UsernameNotFoundException("User not found with this userId "+userId);
        }
    }

    @Override
    public void deleteUser(Long userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()) {
            userRepository.deleteById(userId);
        }else
            throw new UsernameNotFoundException("User not found with userId "+userId);
    }

    @Override
    public void logoutUser() {
        SecurityContextHolder.clearContext();
    }
}
