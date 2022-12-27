package com.backend.starter.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequest {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
