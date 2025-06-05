package com.marketplace.user_auth.service;

import com.marketplace.user_auth.dto.request.LoginRequestDTO;
import com.marketplace.user_auth.dto.request.RegisterRequestDTO;
import com.marketplace.user_auth.dto.response.AuthResponse;
import com.marketplace.user_auth.entity.Token;
import com.marketplace.user_auth.entity.User;
import com.marketplace.user_auth.entity.UserStatus;
import com.marketplace.user_auth.entity.UserType;
import com.marketplace.user_auth.mapper.AuthMapper;
import com.marketplace.user_auth.mapper.UserMapper;
import com.marketplace.user_auth.repository.TokenRepository;
import com.marketplace.user_auth.repository.UserRepository;
import com.marketplace.user_auth.repository.UserStatusRepository;
import com.marketplace.user_auth.repository.UserTypeRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Optional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthMapper authMapper;
    private final JwtTokenService jwtTokenService;
    private final UserTypeRepository userTypeRepository;
    private final UserStatusRepository userStatusRepository;
    private final TokenRepository tokenRepository;

    AuthService(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper,
            AuthMapper authMapper,
            JwtTokenService jwtTokenService,
            UserTypeRepository userTypeRepository,
            UserStatusRepository userStatusRepository,
            TokenRepository tokenRepository
    ){
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.authMapper = authMapper;
        this.jwtTokenService = jwtTokenService;
        this.userTypeRepository = userTypeRepository;
        this.userStatusRepository = userStatusRepository;
        this.tokenRepository = tokenRepository;
    }

    public AuthResponse register(RegisterRequestDTO registerRequestDTO) {

        User user = userMapper.convert(registerRequestDTO);
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setStatus(userStatusRepository.findById(UserStatus.Status.ACTIVE.getValue()).orElse(null));
        user.setType(userTypeRepository.findById(UserType.Type.CUSTOMER_USER.getValue()).orElse(null));
        userRepository.save(user);
        return login(user);
    }

    public AuthResponse login(LoginRequestDTO loginRequestDTO) {

        Authentication authenticated = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmail(),
                        loginRequestDTO.getPassword()
                ));
        if (!authenticated.isAuthenticated()) {
            throw new BadCredentialsException("Bad credentials");
        }

        UserDetails userDetails = (UserDetails) authenticated.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        return  login(user);
    }

    public AuthResponse refreshToken(String headerAuthorization, String cookieRefreshToken) {

        String refreshToken = jwtTokenService.extractFromBearer(headerAuthorization);

        refreshToken = Optional.ofNullable(refreshToken).orElse(cookieRefreshToken);

        final Claims claims;
        try {
            claims = jwtTokenService.extractClaims(refreshToken);
        } catch (SignatureException e){
            throw new BadCredentialsException("Invalid JWT signature");
        }

        if (jwtTokenService.isClaimsExpired(claims)) {
            throw new BadCredentialsException("JWT token expired");
        }

        Token token = tokenRepository.findByRefreshToken(refreshToken);
        if (token == null) {
            throw new BadCredentialsException("RefreshToken Not Found");
        }

        User user = token.getUser();
        tokenRepository.delete(token);

        return login(user);
    }

    public AuthResponse login(User user) {

        Token token = new Token();
        token.setAccessToken(jwtTokenService.generateAccessToken(user));
        token.setRefreshToken(jwtTokenService.generateRefreshToken(user));
        token.setUser(user);
        tokenRepository.save(token);

        AuthResponse authResponse = authMapper.convert(user);
        authResponse.setAccessToken(token.getAccessToken());
        authResponse.setRefreshToken(token.getRefreshToken());

        return authResponse;
    }
}
