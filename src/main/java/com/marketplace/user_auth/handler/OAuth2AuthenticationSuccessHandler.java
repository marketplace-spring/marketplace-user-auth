package com.marketplace.user_auth.handler;
import com.marketplace.user_auth.manager.AuthManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthManager authManager;
    OAuth2AuthenticationSuccessHandler(@Lazy AuthManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String providerId = oauth2User.getName();

        String providerName = null;
        if (authentication instanceof OAuth2AuthenticationToken oauth2Token) {
            providerName = oauth2Token.getAuthorizedClientRegistrationId();
        }

        authManager.oAuth2successHandler(response, email, name, providerId, providerName);
    }
}
