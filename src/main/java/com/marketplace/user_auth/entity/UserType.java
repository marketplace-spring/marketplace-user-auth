package com.marketplace.user_auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserType {
    public enum Type {
        CUSTOMER_USER((short) 1), MERCHANT_USER((short) 2), ADMIN_USER((short) 3);
        private Short value;
        Type(Short value) {
            this.value = value;
        }

        public Short getValue() {
            return value;
        }
    }

    @Id
    private Short id;

    private String name;
}
