package com.marketplace.user_auth.filter;

import com.marketplace.user_auth.service.JwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final UserDetailsService userDetailsService;

    JwtAuthenticationFilter(
            JwtTokenService jwtTokenService,
            UserDetailsService userDetailsService
    ) {
        this.jwtTokenService = jwtTokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws IOException, ServletException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt = jwtTokenService.extractFromBearer(authHeader);

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        final Claims claims;

        try {
             claims = jwtTokenService.extractClaims(jwt);
        } catch (SignatureException e){
            throw new BadCredentialsException("Invalid JWT signature");
        }

        if (jwtTokenService.isClaimsExpired(claims)) {
            throw new BadCredentialsException("JWT token expired");
        }

        final String userEmail = claims.getSubject();

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}