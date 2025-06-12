package com.marketplace.user_auth.controller;

import com.marketplace.user_auth.dto.request.LoginRequestDTO;
import com.marketplace.user_auth.dto.request.RefreshTokenRequestDTO;
import com.marketplace.user_auth.dto.request.RegisterRequestDTO;
import com.marketplace.user_auth.dto.response.AuthResponse;
import com.marketplace.user_auth.manager.AuthManager;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthManager authManager;

    AuthController(
            AuthManager authManager
    ){
        this.authManager = authManager;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody @Valid RegisterRequestDTO registerRequestDTO
            ) {
        return authManager.register(registerRequestDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Valid LoginRequestDTO loginRequestDTO
    ){
        return  authManager.login(loginRequestDTO);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestBody  @Valid RefreshTokenRequestDTO refreshTokenRequestDTO,
            @CookieValue(name = "refresh_token", required = false) String cookieRefreshToken
    ) {
        return authManager.refreshToken(refreshTokenRequestDTO.getRefreshToken(), cookieRefreshToken);
    }
}
