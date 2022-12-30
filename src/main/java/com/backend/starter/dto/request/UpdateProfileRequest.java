package com.backend.starter.dto.request;

import com.backend.starter.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileRequest {
    private Long userId;
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
