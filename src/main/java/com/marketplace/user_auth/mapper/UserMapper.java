package com.marketplace.user_auth.mapper;

import com.marketplace.user_auth.dto.request.RegisterRequestDTO;
import com.marketplace.user_auth.dto.response.UserResponseDTO;
import com.marketplace.user_auth.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public UserResponseDTO convert(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setFirstName(user.getFirstName());
        userResponseDTO.setLastName(user.getLastName());
        userResponseDTO.setPhone(user.getPhone());
        return userResponseDTO;
    }

    public User convert(RegisterRequestDTO requestDTO) {
        User user = new User();
        user.setEmail(requestDTO.getEmail());
        user.setFirstName(requestDTO.getFirstName());
        user.setLastName(requestDTO.getLastName());
        user.setPhone(requestDTO.getPhone());
        return user;
    }
}
