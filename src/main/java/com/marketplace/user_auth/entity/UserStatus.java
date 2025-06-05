package com.marketplace.user_auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserStatus {
    public enum Status {
        PENDING((short) 1), ACTIVE((short) 2), INACTIVE((short) 3);
        private Short value;
        Status(Short value) {
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
