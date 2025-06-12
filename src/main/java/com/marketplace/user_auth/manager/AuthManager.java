package com.marketplace.user_auth.manager;

import com.marketplace.user_auth.dto.UserCreateDTO;
import com.marketplace.user_auth.dto.request.LoginRequestDTO;
import com.marketplace.user_auth.dto.request.RegisterRequestDTO;
import com.marketplace.user_auth.dto.response.AuthResponse;
import com.marketplace.user_auth.dto.response.TokenResponseDTO;
import com.marketplace.user_auth.dto.response.UserResponseDTO;
import com.marketplace.user_auth.entity.OAuth2Provider;
import com.marketplace.user_auth.mapper.AuthMapper;
import com.marketplace.user_auth.mapper.UserMapper;
import com.marketplace.user_auth.service.AuthService;
import com.marketplace.user_auth.service.OAuth2ProviderService;
import com.marketplace.user_auth.service.TokenService;
import com.marketplace.user_auth.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthManager {

    @Value("${jwt.refresh.token.expiration.minute}")
    private long jwtRefreshTokenExpirationMinute;

    @Value("${app.after.login.redirect-uri}")
    private String afterLoginRedirectUri;

    private final UserService userService;
    private final TokenService tokenService;
    private final OAuth2ProviderService oAuth2ProviderService;
    private final UserMapper userMapper;
    private final AuthMapper authMapper;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    AuthManager(
            UserService userService,
            OAuth2ProviderService oAuth2ProviderService,
            TokenService tokenService,
            UserMapper userMapper,
            AuthMapper authMapper,
            AuthenticationManager authenticationManager,
            AuthService authService
    ){
        this.userService = userService;
        this.tokenService = tokenService;
        this.oAuth2ProviderService = oAuth2ProviderService;
        this.userMapper = userMapper;
        this.authMapper = authMapper;
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }

    public ResponseEntity<AuthResponse> register(RegisterRequestDTO registerRequestDTO) {

        UserCreateDTO userCreateDTO = userMapper.convert(registerRequestDTO);
        UserResponseDTO userResponseDTO = userService.findUserByEmail(registerRequestDTO.getEmail());

        if (userResponseDTO != null) {
            throw new BadCredentialsException("Invalid email or password");
        }

        userResponseDTO = userService.create(userCreateDTO);
        return getAuthResponseResponseEntity(userResponseDTO);
    }

    public void oAuth2successHandler(HttpServletResponse response, String email, String name, String provider, String providerId) throws IOException {

        UserCreateDTO userCreateDTO = userMapper.convertToCreateDTO(
                email, name, null, null, UUID.randomUUID().toString()
        );

        UserResponseDTO userResponseDTO = userService.findUserByEmail(email);
        OAuth2Provider oAuth2Provider = oAuth2ProviderService.findByProviderId(providerId);

        if (userResponseDTO == null) {

            userResponseDTO = userService.create(userCreateDTO);
        }

        if(oAuth2Provider == null) {
            oAuth2ProviderService.create(provider, providerId, userResponseDTO.getId());
        }

        TokenResponseDTO tokenResponse = authService.generateToken(userResponseDTO);
        AuthResponse authResponse = authMapper.convert(userResponseDTO, tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());

        Cookie cookie = new Cookie("refresh_token", authResponse.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) jwtRefreshTokenExpirationMinute * 60);

        response.addCookie(cookie);
        response.sendRedirect(afterLoginRedirectUri);
    }

    public ResponseEntity<AuthResponse> login(LoginRequestDTO loginRequestDTO) {

        Authentication authenticated = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmail(),
                        loginRequestDTO.getPassword()
                ));

        UserDetails userDetails = (UserDetails) authenticated.getPrincipal();
        var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        UserResponseDTO userResponseDTO = userService.findUserByEmail(userDetails.getUsername());
        return getAuthResponseResponseEntity(userResponseDTO);
    }

    public ResponseEntity<AuthResponse> refreshToken(String payloadRefreshToken, String cookieRefreshToken) {

        String refreshToken = Optional.ofNullable(payloadRefreshToken).orElse(cookieRefreshToken);

        TokenResponseDTO oldTokenResponseDTO = authService.validateRefreshToken(refreshToken);
        tokenService.deleteById(oldTokenResponseDTO.getId());
        TokenResponseDTO tokenResponse = authService.generateToken(oldTokenResponseDTO.getUser());
        AuthResponse authResponse = authMapper.convert(tokenResponse.getUser(), tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());

        HttpHeaders headers = new HttpHeaders();
        addCookie(headers, authResponse.getRefreshToken());
        return new ResponseEntity<>(authResponse, headers, HttpStatus.OK);
    }

    private ResponseEntity<AuthResponse> getAuthResponseResponseEntity(UserResponseDTO userResponseDTO) {
        TokenResponseDTO tokenResponse = authService.generateToken(userResponseDTO);
        AuthResponse authResponse = authMapper.convert(userResponseDTO, tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());

        HttpHeaders headers = new HttpHeaders();
        addCookie(headers, authResponse.getRefreshToken());

        return new ResponseEntity<>(authResponse, headers, HttpStatus.OK);
    }

    private void addCookie(HttpHeaders headers, String token) {
        headers.add(HttpHeaders.SET_COOKIE, ResponseCookie
                .from("refresh_token", token)
                .httpOnly(true)
                .path("/")
                .maxAge(jwtRefreshTokenExpirationMinute * 60)
                .build().toString());
    }

}
