package com.marketplace.user_auth.service;

import com.marketplace.user_auth.dto.TokenCreateDto;
import com.marketplace.user_auth.dto.response.AuthResponse;
import com.marketplace.user_auth.dto.response.TokenResponseDTO;
import com.marketplace.user_auth.dto.response.UserResponseDTO;
import com.marketplace.user_auth.mapper.TokenMapper;
import com.marketplace.user_auth.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtTokenUtil jwtTokenUtil;
    private final TokenService tokenService;
    private final TokenMapper tokenMapper;

    AuthService(
            JwtTokenUtil jwtTokenUtil,
            TokenService tokenService,
            TokenMapper tokenMapper
    ){
        this.jwtTokenUtil = jwtTokenUtil;
        this.tokenService = tokenService;
        this.tokenMapper = tokenMapper;
    }

    public TokenResponseDTO validateRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new BadCredentialsException("Invalid refresh_token");
        }

        final Claims claims;
        try {
            claims = jwtTokenUtil.extractClaims(refreshToken);
        } catch (SignatureException e){
            throw new BadCredentialsException("Invalid JWT signature");
        }

        if (jwtTokenUtil.isClaimsExpired(claims)) {
            throw new BadCredentialsException("JWT token expired");
        }

        TokenResponseDTO tokenResponseDTO = tokenService.findByRefreshToken(refreshToken);
        if (tokenResponseDTO == null) {
            throw new BadCredentialsException("RefreshToken Not Found");
        }

        return tokenResponseDTO;
    }

    public TokenResponseDTO generateToken(UserResponseDTO userResponseDTO) {

        TokenCreateDto tokenCreateDto = tokenMapper.convert(
                jwtTokenUtil.generateAccessToken(userResponseDTO),
                jwtTokenUtil.generateRefreshToken(userResponseDTO)
        );

        return tokenService.create(tokenCreateDto, userResponseDTO.getId());
    }
}
