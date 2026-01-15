package com.marketplace.user_auth.dto.request;

import com.marketplace.user_auth.validator.constraints.Phone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @Pattern(regexp = "^[\\p{L}]+(?:[ ]\\p{L}+)*$", message = "{customer.create.firstName.only-letter}")
    @NotBlank
    @Size(min = 2, max = 64)
    private String firstName;

    @Pattern(regexp = "^[\\p{L}]+$", message = "{customer.create.lastName.only-letter}")
    @Size(min = 2, max = 64)
    private String lastName;

    @Email()
    @NotBlank
    private String email;

    @Size(min = 8, max = 256)
    @NotBlank
    private String password;

    @Pattern(regexp = "^\\d{11,13}$", message = "Invalid phone number")
    private String phone;

}
