package com.marketplace.user_auth.service;

import com.marketplace.user_auth.entity.User;
import com.marketplace.user_auth.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<GrantedAuthority> roles = List.of(
                new SimpleGrantedAuthority(user.getType().getName())
        );

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                roles
        );
    }
}
