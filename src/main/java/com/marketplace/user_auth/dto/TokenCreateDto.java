package com.marketplace.user_auth.dto;

import lombok.Data;

@Data
public class TokenCreateDto {
    private String accessToken;
    private String refreshToken;
}
