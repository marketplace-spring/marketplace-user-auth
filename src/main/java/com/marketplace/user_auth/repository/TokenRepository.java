package com.marketplace.user_auth.repository;

import com.marketplace.user_auth.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByRefreshToken(String refreshToken);
}
