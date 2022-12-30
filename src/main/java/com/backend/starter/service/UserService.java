package com.backend.starter.service;

import com.backend.starter.dto.request.*;
import com.backend.starter.dto.response.*;

import java.security.Principal;
import java.util.List;

public interface UserService{
    RegisterResponse save(RegisterRequest request);
    AddUserResponse add(AddUserRequest request);
    LoginResponse login(LoginRequest request);
    List<UserDetails> getAllUser();
    UserDetails getUser(long id);
    UserProfile getUserProfile(long id);
    void deleteUserById(Long id);
    UpdateProfileResponse updateUser(UpdateProfileRequest request);

    void resetPassword(PasswordResetRequest user, Principal principal);
    boolean activateDeactivateUserAccount(long id);
    boolean enableDisableUserAccount(long id);
}
