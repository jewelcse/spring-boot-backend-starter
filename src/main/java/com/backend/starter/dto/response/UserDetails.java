package com.backend.starter.dto.response;

import com.backend.starter.entity.Role;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDetails {
    private String username;
    private String email;
    private boolean isEnabled;
    private boolean isNonLocked;
    private Set<Role> roles;
}
