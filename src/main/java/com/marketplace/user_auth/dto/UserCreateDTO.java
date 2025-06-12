package com.marketplace.user_auth.dto;

import com.marketplace.user_auth.entity.UserStatus;
import com.marketplace.user_auth.entity.UserType;
import lombok.Data;

@Data
public class UserCreateDTO {
    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phone;

    private UserStatus.Status status;

    private UserType.Type type;

    private String provider;

    private String providerId;
}
