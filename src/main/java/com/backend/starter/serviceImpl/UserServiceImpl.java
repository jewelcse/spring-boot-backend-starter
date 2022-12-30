package com.backend.starter.serviceImpl;

import com.backend.starter.dto.request.*;
import com.backend.starter.dto.response.*;
import com.backend.starter.entity.Profile;
import com.backend.starter.entity.Role;
import com.backend.starter.entity.RoleType;
import com.backend.starter.entity.User;
import com.backend.starter.exception.LoginException;
import com.backend.starter.exception.RoleNotFoundException;
import com.backend.starter.exception.UserAlreadyExistsException;
import com.backend.starter.exception.UserNotFoundException;
import com.backend.starter.mapper.UserMapper;
import com.backend.starter.repository.RoleRepository;
import com.backend.starter.repository.UserRepository;
import com.backend.starter.security.jwt.JwtUtil;
import com.backend.starter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;

import static com.backend.starter.util.UtilMethods.generateRandomString;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder encoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    @Transactional
    @Override
    public RegisterResponse save(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())){
            throw new UserAlreadyExistsException("Already have the email!");
        }
        if (userRepository.existsByUsername(request.getUsername())){
            throw new UserAlreadyExistsException("Already have the username!");
        }
        //user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setEnabled(false);
        user.setNonLocked(true);
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRoles(checkRoles(request.getRole()));
        //profile
        Profile profile = new Profile();
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());

        user.setProfile(profile);
        userRepository.save(user);
        //response
        return RegisterResponse.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .username(request.getUsername())
                .isAccountNonLocked(true)
                .isAccountVerified(false) // enabled is renamed here with verified attribute
                .build();
    }

    @Override
    public AddUserResponse add(AddUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())){
            throw new UserAlreadyExistsException("Already have this email!");
        }
        //generate random username and password
        String randomUserPassword = generateRandomString(8);
        String randomUsername = request.getFirstName().toLowerCase() + "@" + generateRandomString(4);
        //user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(randomUsername);
        user.setPassword(encoder.encode(randomUserPassword));
        user.setNonLocked(true);
        user.setEnabled(true);
        user.setRoles(checkRoles(request.getRole()));
        //profile
        Profile profile = new Profile();
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());

        user.setProfile(profile);
        userRepository.save(user);
        return AddUserResponse
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .username(randomUsername)
                .build();

    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        return LoginResponse.builder()
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .token(jwt)
                .type("Bearer")
                .role(roles.get(0))
                .build();

    }


    @Override
    public List<UserDetails> getAllUser() {
        return userMapper.toListOfUserDetails(userRepository.findAll());
    }

    @Override
    public UserDetails getUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("USER NOT FOUND!"));
        return userMapper.toUserDetails(user);
    }


    @Override
    public UserProfile getUserProfile(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("USER NOT FOUND!"));
        return UserProfile
                .builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .isEnabled(user.isEnabled())
                .isNonLocked(user.isNonLocked())
                .firstName(user.getProfile().getFirstName())
                .lastName(user.getProfile().getLastName())
                .phoneNumber(user.getProfile().getPhoneNumber())
                .street(user.getProfile().getStreet())
                .state(user.getProfile().getState())
                .zipCode(user.getProfile().getZipCode())
                .gender(user.getProfile().getGender())
                .city(user.getProfile().getCity())
                .country(user.getProfile().getCountry())
                .dateOfBirth(user.getProfile().getDateOfBirth())
                .address1(user.getProfile().getAddress1())
                .address2(user.getProfile().getAddress2())
                .role(getRole(user.getRoles()))
                .build();
    }


    @Override
    public void deleteUserById(Long id) {
        boolean doesExists = userRepository.existsById(id);
        if (!doesExists) {
            throw new UserNotFoundException("NOT FOUND!");
        }
        userRepository.deleteById(id);

    }

    @Transactional
    @Override
    public UpdateProfileResponse updateUser(UpdateProfileRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND!"));

        Profile profile = user.getProfile();
        profile.setAddress1(request.getAddress1());
        profile.setAddress2(request.getAddress2());
        profile.setStreet(request.getStreet());
        profile.setCity(request.getCity());
        profile.setCountry(request.getCountry());
        profile.setState(request.getState());
        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setGender(profile.getGender());
        profile.setZipCode(request.getZipCode());
        profile.setPhoneNumber(request.getPhoneNumber());

        userRepository.save(user);
        return userMapper.toUserProfile(profile);
    }


    @Transactional
    @Override
    public boolean activateDeactivateUserAccount(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("USER NOT FOUND!"));
        user.setId(user.getId());
        user.setNonLocked(!user.isNonLocked());
        return userRepository.save(user).isNonLocked();
    }

    @Override
    public boolean enableDisableUserAccount(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("USER NOT FOUND!"));
        user.setId(user.getId());
        user.setEnabled(!user.isEnabled());
        return userRepository.save(user).isEnabled();
    }


    @Override
    public void resetPassword(PasswordResetRequest request, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("USER NOT FOUND"));

        if (!encoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new LoginException("INCORRECT THE CURRENT PASSWORD!");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new LoginException("CONFIRM PASSWORD DOESN'T MATCH WITH THE NEW PASSWORD!");
        }
        user.setPassword(encoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private Set<Role> checkRoles(String stringRole) {
        Set<Role> roles = new HashSet<>();
        if (stringRole == null) {
            Role defaultRole = roleRepository.findByName(RoleType.ROLE_USER)
                    .orElseThrow(() -> new RoleNotFoundException(RoleType.ROLE_USER + " DOESN'T EXIST!"));
            roles.add(defaultRole);
        } else {
            switch (stringRole) {
                case "ROLE_CONTRIBUTOR" -> {
                    Role trainerRole = roleRepository.findByName(RoleType.ROLE_CONTRIBUTOR)
                            .orElseThrow(() -> new RoleNotFoundException(RoleType.ROLE_CONTRIBUTOR + " DOESN'T EXIST!"));
                    roles.add(trainerRole);
                }
                case "ROLE_USER" -> {
                    Role traineeRole = roleRepository.findByName(RoleType.ROLE_USER)
                            .orElseThrow(() -> new RoleNotFoundException(RoleType.ROLE_USER + " DOESN'T EXIST!"));
                    roles.add(traineeRole);
                }
            }
        }
        return roles;
    }

    private String getRole(Set<Role> roles){
        Optional<Role> role = roles.stream().findFirst();
        if ((role.isEmpty())){
            throw new RoleNotFoundException("NOT FOUND!");
        }
        return role.get().getName().toString();
    }



}
