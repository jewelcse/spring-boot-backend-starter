package com.backend.starter.dto.response;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddUserResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String username;
}
