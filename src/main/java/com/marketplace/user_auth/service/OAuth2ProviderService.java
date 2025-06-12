package com.marketplace.user_auth.service;

import com.marketplace.user_auth.entity.OAuth2Provider;
import com.marketplace.user_auth.repository.OAuth2ProviderRepository;
import com.marketplace.user_auth.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class OAuth2ProviderService {

    private final OAuth2ProviderRepository oAuth2ProviderRepository;
    private final UserRepository userRepository;

    OAuth2ProviderService(
            OAuth2ProviderRepository oAuth2ProviderRepository,
            UserRepository userRepository
    ){
        this.oAuth2ProviderRepository = oAuth2ProviderRepository;
        this.userRepository = userRepository;
    }

    public OAuth2Provider findByProviderId(String providerId){
        return oAuth2ProviderRepository.findById(providerId).orElse(null);
    }

    public OAuth2Provider create(String provider, String providerId, Long userId) {
        OAuth2Provider oAuth2Provider = new OAuth2Provider();
        oAuth2Provider.setId(providerId);
        oAuth2Provider.setName(provider);
        oAuth2Provider.setUser(userRepository.findById(userId).orElse(null));
        oAuth2ProviderRepository.save(oAuth2Provider);
        return oAuth2Provider;
    }
}
