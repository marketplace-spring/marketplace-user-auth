package com.marketplace.user_auth.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Data
@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @Column(nullable = false, length = 1024)
    private String accessToken;

    @Column(nullable = false, length = 1024)
    private String refreshToken;

    @Column(nullable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;
}
