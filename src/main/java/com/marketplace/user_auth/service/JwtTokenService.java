package com.marketplace.user_auth.service;

import com.marketplace.user_auth.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Service
public class JwtTokenService {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access.token.expiration.minute}")
    private long jwtAccessTokenExpirationMinute;

    @Value("${jwt.refresh.token.expiration.minute}")
    private long jwtRefreshTokenExpirationMinute;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    private String generateToken(User user, HashMap<String, Object> claims, Long expirationMinute) {

        claims.put("jti", UUID.randomUUID().toString());
        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMinute * 60 * 1000))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(User user) {
        HashMap<String, Object> claims = new HashMap<>();
        return generateToken(user, claims, jwtRefreshTokenExpirationMinute);
    }

    public String generateAccessToken(User user) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("roles", user.getType().getName());
        return generateToken(user, claims, jwtAccessTokenExpirationMinute);
    }

    public boolean isClaimsExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    public String extractFromBearer(String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
