package com.backend.starter.service;

import com.backend.starter.dto.request.RegisterRequest;
import com.backend.starter.dto.request.UserAddRequest;
import com.backend.starter.dto.request.UserUpdateRequest;
import com.backend.starter.dto.response.RegisterResponse;
import com.backend.starter.dto.response.UserAddResponse;
import com.backend.starter.entity.User;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Optional;

public interface UserService{
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String userName);
    boolean existsByEmail(String email);
    RegisterResponse save(RegisterRequest request);
    UserAddResponse save(UserAddRequest request);

    List<User> getAllUser();
    void deleteUserById(Long id);
    User updateUser(UserUpdateRequest userUpdateRequest);
    void activateDeactivateUserAccount(User user);
    Optional<User> existsByUserId(long id);
    Optional<User> findById(long id);
    User resetPassword(User user);
}
