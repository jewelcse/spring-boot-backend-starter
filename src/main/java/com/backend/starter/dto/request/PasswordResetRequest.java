package com.backend.starter.dto.request;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordResetRequest {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
