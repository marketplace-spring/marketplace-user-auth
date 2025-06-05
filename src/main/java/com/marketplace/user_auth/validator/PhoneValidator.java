package com.marketplace.user_auth.validator;

import com.marketplace.user_auth.validator.constraints.Phone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return (value == null || value.isEmpty()) || value.matches("\\d{12}");
    }
}
