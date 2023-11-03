package com.francis.Security.repositories;

import com.francis.Security.models.entities.UserEntity;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Boolean existsByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsernameOrEmail(String username,String email);
    Optional<UserEntity> findByUsername(String username);
    Boolean existsByEmail(String email);
}
