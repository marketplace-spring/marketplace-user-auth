package com.marketplace.user_auth.service;

import com.marketplace.user_auth.dto.TokenCreateDto;
import com.marketplace.user_auth.dto.response.TokenResponseDTO;
import com.marketplace.user_auth.entity.Token;
import com.marketplace.user_auth.mapper.TokenMapper;
import com.marketplace.user_auth.repository.TokenRepository;
import com.marketplace.user_auth.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final TokenMapper tokenMapper;
    private final UserRepository userRepository;

    TokenService(
            TokenRepository tokenRepository,
            UserRepository userRepository,
            TokenMapper tokenMapper
    ){
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.tokenMapper = tokenMapper;
    }

    public TokenResponseDTO create(TokenCreateDto tokenCreateDto, Long userId) {
        Token token = tokenMapper.convert(tokenCreateDto);
        token.setUser(userRepository.findById(userId).orElse(null));
        tokenRepository.save(token);
        return tokenMapper.convert(token);
    }

    public TokenResponseDTO findByRefreshToken(String refreshToken){
        return tokenRepository.findByRefreshToken(refreshToken)
                .map(tokenMapper::convert).orElse(null);
    }

    public void  deleteById(Long id){
        tokenRepository.deleteById(id);
    }
}
