package com.marketplace.user_auth.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
