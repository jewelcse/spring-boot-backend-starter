package com.backend.starter.controller;

import com.backend.starter.dto.request.*;
import com.backend.starter.dto.response.*;
import com.backend.starter.exception.UserNotFoundException;
import com.backend.starter.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/sign-in")
    public ResponseEntity<LoginResponse> signIn(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<RegisterResponse> signUp(@Valid @RequestBody RegisterRequest request) {
        return new ResponseEntity<>(userService.save(request), HttpStatus.CREATED);
    }

    @PostMapping("/users")
    public ResponseEntity<AddUserResponse> addUser(@Valid @RequestBody AddUserRequest request) {
        return ResponseEntity.ok(userService.add(request));
    }

    @PostMapping("/users/password-reset")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request, Principal principal) {
        userService.resetPassword(request,principal);
        return ResponseEntity.ok("Password reset successful!");
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDetails>> getUsers() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PutMapping("/users/profile")
    public ResponseEntity<UpdateProfileResponse> updateProfile(@RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateUser(request));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDetails> getUser(@PathVariable("id") long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/users/profile/{id}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable("id") long userId) {
        return new ResponseEntity<>(userService.getUserProfile(userId), HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable("id") long id){
        userService.deleteUserById(id);
        return ResponseEntity.ok(new MessageResponse("user remove successful!"));
    }

    @PostMapping("/activate-deactivate/user/account")
    public ResponseEntity<MessageResponse> activateDeActivateUserAccount(@RequestParam long id){
        if (userService.activateDeactivateUserAccount(id)) {
            return ResponseEntity.ok(new MessageResponse("user account activated!"));
        } else {
            return ResponseEntity.ok(new MessageResponse("user account deactivated!"));
        }
    }

    @PostMapping("/enable-disable/user/account")
    public ResponseEntity<MessageResponse> enableDisableUserAccount(@RequestParam long id){
        if (userService.enableDisableUserAccount(id)) {
            return ResponseEntity.ok(new MessageResponse("USER ACCOUNT ENABLED!!"));
        } else {
            return ResponseEntity.ok(new MessageResponse("USER ACCOUNT DISABLED!"));
        }
    }
}
