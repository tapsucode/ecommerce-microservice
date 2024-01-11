package com.phamanh.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.WebFilter;

@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange()
                .pathMatchers("/public/**").permitAll()
                .pathMatchers("/admin/**").hasRole("ROLE_ADMIN")  // Yêu cầu vai trò ADMIN cho URL bắt đầu bằng "/admin/"
                .pathMatchers("/user/**").hasRole("ROLE_CUSTOMER")    // Yêu cầu vai trò USER cho URL bắt đầu bằng "/user/"
                .pathMatchers("/sale/**").hasRole("ROLE_SALE")    // Yêu cầu vai trò SALE cho URL bắt đầu bằng "/sale/"
                .anyExchange().authenticated()
                .and()
                .csrf().disable()
                .cors().disable()
                .headers().frameOptions().disable();

        return http.build();
    }
}
