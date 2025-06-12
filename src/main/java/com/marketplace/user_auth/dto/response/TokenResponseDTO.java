package com.marketplace.user_auth.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class TokenResponseDTO {
    private Long id;
    private String accessToken;
    private String refreshToken;
    @JsonIgnore
    private UserResponseDTO user;
}
