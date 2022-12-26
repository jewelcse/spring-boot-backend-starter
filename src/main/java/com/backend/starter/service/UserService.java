package com.backend.starter.service;

import com.backend.starter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserService{
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
