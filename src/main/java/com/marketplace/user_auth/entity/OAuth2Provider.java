package com.marketplace.user_auth.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "id", "name" }) })
public class OAuth2Provider {

    // google veya githubdan gelen ID
    @Id
    @Column(length = 256)
    private String id;

    // "google" veya "github"
    @Column(length = 64)
    private String name;

    @ManyToOne
    private User user;
}
