package com.marketplace.user_auth.repository;

import com.marketplace.user_auth.entity.OAuth2Provider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuth2ProviderRepository extends JpaRepository<OAuth2Provider, String> {
}
