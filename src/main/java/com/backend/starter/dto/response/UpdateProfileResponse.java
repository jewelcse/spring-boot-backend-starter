package com.backend.starter.dto.response;

import com.backend.starter.entity.Gender;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProfileResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Gender gender;
    private Date dateOfBirth;
    private String address1;
    private String address2;
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;

}
