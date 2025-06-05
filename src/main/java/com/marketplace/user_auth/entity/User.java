package com.marketplace.user_auth.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String firstName;

    @Column(length = 64)
    private String lastName;

    @Column(nullable = false, unique = true, length = 180)
    private String email;

    @Column(nullable = true, length = 256)
    private String password;

    @Column(length = 12)
    private String phone;

    @ManyToOne
    private UserStatus status;

    @ManyToOne
    private UserType type;

    @OneToMany(mappedBy = "user")
    List<OAuth2Provider> providers = new ArrayList<>();

    @Column(nullable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private OffsetDateTime updatedAt;

    private OffsetDateTime lastLoginAt;
}
