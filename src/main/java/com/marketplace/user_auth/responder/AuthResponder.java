package com.marketplace.user_auth.responder;

import com.marketplace.user_auth.dto.response.AuthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthResponder {

    @Value("${jwt.refresh.token.expiration.minute}")
    private long jwtRefreshTokenExpirationMinute;

    public ResponseEntity<AuthResponse> register(AuthResponse authResponse) {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, getTokenCookie(authResponse).toString());
        return new ResponseEntity<>(authResponse, headers, HttpStatus.OK);
    }

    public ResponseEntity<AuthResponse> login(AuthResponse authResponse) {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, getTokenCookie(authResponse).toString());
        return new ResponseEntity<>(authResponse, headers, HttpStatus.OK);
    }

    private ResponseCookie getTokenCookie(AuthResponse authResponse) {
        return ResponseCookie
                .from("token", authResponse.getRefreshToken())
                .httpOnly(true)
                //.secure(true)
                .path("/")
                .maxAge(jwtRefreshTokenExpirationMinute / 60)
                .build();
    }
}
