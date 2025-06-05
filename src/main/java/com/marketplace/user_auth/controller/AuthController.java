package com.marketplace.user_auth.controller;

import com.marketplace.user_auth.dto.request.LoginRequestDTO;
import com.marketplace.user_auth.dto.request.RegisterRequestDTO;
import com.marketplace.user_auth.dto.response.AuthResponse;
import com.marketplace.user_auth.responder.AuthResponder;
import com.marketplace.user_auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthResponder authResponder;

    AuthController(
            AuthService authService,
            AuthResponder authResponder
    ){
        this.authService = authService;
        this.authResponder = authResponder;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody @Valid RegisterRequestDTO registerRequestDTO
            ) {
        AuthResponse authResponse =  authService.register(registerRequestDTO);
        return authResponder.register(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Valid LoginRequestDTO loginRequestDTO
    ){
        AuthResponse authResponse = authService.login(loginRequestDTO);
        return authResponder.login(authResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshAccessToken(
            @RequestHeader("Authorization") String headerAuthorization,
            @CookieValue(name = "token", required = false) String cookieRefreshToken
    ) {
        var response = authService.refreshToken(headerAuthorization, cookieRefreshToken);
        return authResponder.login(response);
    }
}
