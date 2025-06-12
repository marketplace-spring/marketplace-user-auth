package com.marketplace.user_auth.mapper;

import com.marketplace.user_auth.dto.TokenCreateDto;
import com.marketplace.user_auth.dto.response.TokenResponseDTO;
import com.marketplace.user_auth.entity.Token;
import org.springframework.stereotype.Service;

@Service
public class TokenMapper {
    private final UserMapper userMapper;

    TokenMapper(UserMapper userMapper){
        this.userMapper = userMapper;
    }
    public TokenResponseDTO convert(Token token) {
        TokenResponseDTO tokenResponseDTO = new TokenResponseDTO();
        tokenResponseDTO.setId(token.getId());
        tokenResponseDTO.setAccessToken(token.getAccessToken());
        tokenResponseDTO.setRefreshToken(token.getRefreshToken());
        tokenResponseDTO.setUser(userMapper.convert(token.getUser()));
        return tokenResponseDTO;
    }

    public Token convert(TokenCreateDto tokenCreateDto) {
        Token token = new Token();
        token.setAccessToken(tokenCreateDto.getAccessToken());
        token.setRefreshToken(tokenCreateDto.getRefreshToken());
        return token;
    }

    public TokenCreateDto convert(String accessToken, String refreshToken) {
        TokenCreateDto tokenCreateDto = new TokenCreateDto();
        tokenCreateDto.setAccessToken(accessToken);
        tokenCreateDto.setRefreshToken(refreshToken);
        return tokenCreateDto;
    }
}
