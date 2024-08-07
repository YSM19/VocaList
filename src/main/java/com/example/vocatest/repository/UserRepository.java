package com.example.vocatest.repository;

import com.example.vocatest.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);
    UserEntity findByEmail(String email);
}

