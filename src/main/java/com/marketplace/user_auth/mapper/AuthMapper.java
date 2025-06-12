package com.marketplace.user_auth.mapper;

import com.marketplace.user_auth.dto.response.AuthResponse;
import com.marketplace.user_auth.dto.response.TokenResponseDTO;
import com.marketplace.user_auth.dto.response.UserResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class AuthMapper {

    public AuthResponse convert(UserResponseDTO userResponseDTO) {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setId(userResponseDTO.getId());
        authResponse.setFirstName(userResponseDTO.getFirstName());
        authResponse.setLastName(userResponseDTO.getLastName());
        authResponse.setEmail(userResponseDTO.getEmail());
        authResponse.setPhone(userResponseDTO.getPhone());
        return authResponse;
    }

    public AuthResponse convert(UserResponseDTO userResponseDTO, String accessToken, String refreshToken) {
        AuthResponse authResponse = convert(userResponseDTO);
        authResponse.setAccessToken(accessToken);
        authResponse.setRefreshToken(refreshToken);
        return authResponse;
    }
}
