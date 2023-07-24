package com.backend.starter.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginResponse {
    private String token;
    private String type;
    private String username;
    private String email;
    private String role;
}
