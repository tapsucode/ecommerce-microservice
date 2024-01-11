package com.phamanh.apigateway.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            //Verify and extract authority information from the JWT token
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (token != null && token.startsWith("Bearer ")){

                token = token.substring(7);

                jwtUtil.validateToken(token);

            }

            List<GrantedAuthority> authorities = jwtUtil.extractAuthoritiesFromToken(token);

            // Create an Authentication object from authorization information
            Authentication authentication = new UsernamePasswordAuthenticationToken(null, null, authorities);

            // Place the Authentication object in the SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return chain.filter(exchange);
        };


    }


    public static class Config {

    }
}
