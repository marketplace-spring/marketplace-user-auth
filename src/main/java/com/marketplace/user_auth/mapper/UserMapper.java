package com.marketplace.user_auth.mapper;

import com.marketplace.user_auth.dto.UserCreateDTO;
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
        userResponseDTO.setType(user.getType() == null ? null : user.getType().getName());
        return userResponseDTO;
    }

    public UserCreateDTO convert(RegisterRequestDTO registerRequestDTO) {
        return convertToCreateDTO(registerRequestDTO.getEmail(),
                registerRequestDTO.getFirstName(),
                registerRequestDTO.getLastName(),
                registerRequestDTO.getPhone(),
                registerRequestDTO.getPassword());
    }

    public User convert(UserCreateDTO userCreateDTO) {
        User user = new User();
        user.setEmail(userCreateDTO.getEmail());
        user.setFirstName(userCreateDTO.getFirstName());
        user.setLastName(userCreateDTO.getLastName());
        user.setPhone(userCreateDTO.getPhone());
        return user;
    }

    public UserCreateDTO convertToCreateDTO(String email, String firstName, String lastName, String phone, String password) {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setEmail(email);
        userCreateDTO.setFirstName(firstName);
        userCreateDTO.setLastName(lastName);
        userCreateDTO.setPhone(phone);
        userCreateDTO.setPassword(password);
        return userCreateDTO;
    }
}
