package com.marketplace.user_auth.service;

import com.marketplace.user_auth.dto.response.UserResponseDTO;
import com.marketplace.user_auth.mapper.UserMapper;
import com.marketplace.user_auth.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    UserService(
            UserRepository userRepository,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponseDTO findUserById(Long id) {
         return userRepository.findById(id)
                 .map(userMapper::convert)
                 .orElse(null);
    }

    public List<UserResponseDTO> findUserByIds(List<Long> ids) {
        return userRepository.findAllById(ids)
                .stream()
                .map(userMapper::convert)
                .toList();
    }
}
