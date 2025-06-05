package com.marketplace.user_auth.validator.constraints;

import com.marketplace.user_auth.validator.PhoneValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidator.class)
public @interface Phone {
    String message() default "{validator.phone.invalid}";

    Class<?>[] groups() default {};

    String fieldName() default "phone";

    Class<? extends Payload>[] payload() default {};
}
