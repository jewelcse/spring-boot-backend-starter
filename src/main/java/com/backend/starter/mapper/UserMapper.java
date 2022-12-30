package com.backend.starter.mapper;

import com.backend.starter.dto.response.UpdateProfileResponse;
import com.backend.starter.dto.response.UserDetails;
import com.backend.starter.entity.Profile;
import com.backend.starter.entity.Role;
import com.backend.starter.entity.User;
import com.backend.starter.exception.RoleNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class UserMapper {

    public UserDetails toUserDetails(User user){
        if (user==null) return null;
        return UserDetails.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .isEnabled(user.isEnabled())
                .isNonLocked(user.isNonLocked())
                .role(getRole(user.getRoles()))
                .build();
    }

    public List<UserDetails> toListOfUserDetails(List<User> users){
        if (users==null) return null;
        List<UserDetails> list = new ArrayList<>();
        users.forEach(user -> list.add(toUserDetails(user)));
        return list;
    }

    public UpdateProfileResponse toUserProfile(Profile profile){
        if (profile==null) return null;
        return UpdateProfileResponse.builder()
                .address1(profile.getAddress1())
                .address2(profile.getAddress2())
                .street(profile.getStreet())
                .city(profile.getCity())
                .country(profile.getCountry())
                .dateOfBirth(profile.getDateOfBirth())
                .state(profile.getState())
                .gender(profile.getGender())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .phoneNumber(profile.getPhoneNumber())
                .zipCode(profile.getZipCode())
                .build();
    }

    private String getRole(Set<Role> roles){
        Optional<Role> role = roles.stream().findFirst();
        if ((role.isEmpty())){
            throw new RoleNotFoundException("NOT FOUND!");
        }
        return role.get().getName().toString();
    }


}
