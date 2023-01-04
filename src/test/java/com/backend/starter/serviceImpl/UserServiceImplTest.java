package com.backend.starter.serviceImpl;

import com.backend.starter.dto.request.PasswordResetRequest;
import com.backend.starter.dto.response.UserDetails;
import com.backend.starter.dto.response.UserProfile;
import com.backend.starter.entity.*;
import com.backend.starter.mapper.UserMapper;
import com.backend.starter.repository.UserRepository;
import com.backend.starter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

import java.security.Principal;
import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        System.out.println("UserServiceImplTest Test starts!");
    }

    @AfterEach
    void tearDown() {
        System.out.println("UserServiceImplTest Test ends!");
    }

    @Test
    void save() {


    }

    @Test
    void add() {
    }

    @Test
    void login() {
    }

    @Test
    void getAllUser() {
        // Arrange
        Profile p1 = new Profile(1L, "fistname", "lastName", "01930792666",
                Gender.MALE, new Date(), "address1", "address2", "street1", "city1",
                "state1", "country1", "8110");

        User u1 = new User(1L, "username1", "useremail1@gmail.com",
                "$2a$10$bWjl8AP9XR/x42ZM40RiuOSsevm5PrfjMIAupERS.Ya6EgtLQU3oa",
                true, true, null, p1);

        List<User> users = Arrays.asList(u1, u1);


        UserDetails d1 = new UserDetails("username1", "useremail1@gmail.com",
                true, true, null);
        List<UserDetails> uds = List.of(d1, d1);

        // Act
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toListOfUserDetails(users)).thenReturn(uds);

        List<UserDetails> response = userService.getAllUser();


        // Assert
        Assertions.assertEquals(response.size(), uds.size());

    }

    @Test
    void getUser() {

        long id = 1L;
        Profile profile = new Profile(1L, "fistname", "lastName", "01930792666",
                Gender.MALE, new Date(), "address1", "address2", "street1", "city1",
                "state1", "country1", "8110");

        User user = new User(1L, "username1", "useremail1@gmail.com",
                "$2a$10$bWjl8AP9XR/x42ZM40RiuOSsevm5PrfjMIAupERS.Ya6EgtLQU3oa",
                true, true, null, profile);
        UserDetails userDetails = new UserDetails("username1", "useremail1@gmail.com",
                true, true, null);
        // act
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toUserDetails(user)).thenReturn(userDetails);

        UserDetails result = userService.getUser(id);

        // assert
        Assertions.assertEquals(result.getEmail(), userDetails.getEmail());

    }

    @Test
    void getUserProfile() {
        long id = 1L;
        Profile profile = new Profile(1L, "fistname", "lastName", "01930792666",
                Gender.MALE, new Date(), "address1", "address2", "street1", "city1",
                "state1", "country1", "8110");

        Role role = new Role();
        role.setName(RoleType.ROLE_USER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User user = new User(1L, "username1", "useremail1@gmail.com",
                "$2a$10$bWjl8AP9XR/x42ZM40RiuOSsevm5PrfjMIAupERS.Ya6EgtLQU3oa",
                true, true, roles, profile);

        // act
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserProfile result = userService.getUserProfile(id);

        // assert
        Assertions.assertEquals(result.getCountry(), "country1");

    }

    @Test
    void deleteUserById() {

        long id = 1L;
        when(userRepository.existsById(id)).thenReturn(true);
        //doNothing().when(userRepository).deleteById(id);

        userService.deleteUserById(id);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void updateUser() {
    }

    @Test
    void activateDeactivateUserAccount() {
        long id = 1L;
        Profile profile = new Profile(id, "fistname", "lastName", "01930792666",
                Gender.MALE, new Date(), "address1", "address2", "street1", "city1",
                "state1", "country1", "8110");

        User user = new User(id, "username1", "useremail1@gmail.com",
                "$2a$10$bWjl8AP9XR/x42ZM40RiuOSsevm5PrfjMIAupERS.Ya6EgtLQU3oa",
                true, false, null, profile);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        boolean result = userService.activateDeactivateUserAccount(id);

        Assertions.assertTrue(result);

    }

    @Test
    void enableDisableUserAccount() {
        long id = 1L;
        Profile profile = new Profile(id, "fistname", "lastName", "01930792666",
                Gender.MALE, new Date(), "address1", "address2", "street1", "city1",
                "state1", "country1", "8110");

        User user = new User(id, "username1", "useremail1@gmail.com",
                "$2a$10$bWjl8AP9XR/x42ZM40RiuOSsevm5PrfjMIAupERS.Ya6EgtLQU3oa",
                true, false, null, profile);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        boolean result = userService.enableDisableUserAccount(id);

        Assertions.assertFalse(result);
    }

    @Test
    @Ignore
    void resetPassword() {

        String username = "username";
        long id = 1L;

        PasswordResetRequest request = PasswordResetRequest
                .builder()
                .oldPassword("123")
                .newPassword("qwer")
                .confirmPassword("qwer")
                .build();

        Profile profile = new Profile(id, "fistname", "lastName", "01930792666",
                Gender.MALE, new Date(), "address1", "address2", "street1", "city1",
                "state1", "country1", "8110");

        User user = new User(id, username, "useremail1@gmail.com",
                "$2a$10$bWjl8AP9XR/x42ZM40RiuOSsevm5PrfjMIAupERS.Ya6EgtLQU3oa",
                true, false, null, profile);


        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        verify(userService).resetPassword(request,null);

    }
}