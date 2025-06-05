package com.marketplace.user_auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
}
