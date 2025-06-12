package com.marketplace.user_auth.service;

import com.marketplace.user_auth.dto.UserCreateDTO;
import com.marketplace.user_auth.dto.response.UserResponseDTO;
import com.marketplace.user_auth.entity.User;
import com.marketplace.user_auth.entity.UserStatus;
import com.marketplace.user_auth.entity.UserType;
import com.marketplace.user_auth.mapper.UserMapper;
import com.marketplace.user_auth.repository.UserRepository;
import com.marketplace.user_auth.repository.UserStatusRepository;
import com.marketplace.user_auth.repository.UserTypeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserStatusRepository userStatusRepository;
    private final UserTypeRepository userTypeRepository;

    UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            UserStatusRepository userStatusRepository,
            UserTypeRepository userTypeRepository
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userStatusRepository = userStatusRepository;
        this.userTypeRepository = userTypeRepository;
    }

    public UserResponseDTO findUserById(Long id) {
         return userRepository.findById(id)
                 .map(userMapper::convert)
                 .orElse(null);
    }

    public UserResponseDTO findUserByEmail(String email) {
         return userRepository.findByEmail(email)
                 .map(userMapper::convert)
                 .orElse(null);
    }

    public List<UserResponseDTO> findUserByIds(List<Long> ids) {
        return userRepository.findAllById(ids)
                .stream()
                .map(userMapper::convert)
                .toList();
    }

    public UserResponseDTO create(UserCreateDTO userCreateDTO) {
        User user = userMapper.convert(userCreateDTO);
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        user.setStatus(userStatusRepository.findById(UserStatus.Status.ACTIVE.getValue()).orElse(null));
        user.setType(userTypeRepository.findById(UserType.Type.CUSTOMER_USER.getValue()).orElse(null));
        userRepository.save(user);
        return userMapper.convert(user);
    }
}
