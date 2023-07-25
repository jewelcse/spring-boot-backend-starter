package com.backend.starter.serviceImpl;

import com.backend.starter.entity.User;
import com.backend.starter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Line:17: UserDetailsServiceImpl class");

        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("USER NOT FOUND!"));
        return UserDetailsImpl.build(user);
    }
}
