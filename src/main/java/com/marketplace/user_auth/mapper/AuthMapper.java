package com.marketplace.user_auth.mapper;

import com.marketplace.user_auth.dto.response.AuthResponse;
import com.marketplace.user_auth.entity.User;
import org.springframework.stereotype.Service;

@Service
public class AuthMapper {
    public AuthResponse convert(User user) {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setId(user.getId());
        authResponse.setFirstName(user.getFirstName());
        authResponse.setLastName(user.getLastName());
        authResponse.setEmail(user.getEmail());
        authResponse.setPhone(user.getPhone());
        return authResponse;
    }
}
