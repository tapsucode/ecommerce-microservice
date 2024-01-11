package com.phamanh.apigateway.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);


    public static final String SECRET = "akakkqwjwkl,jfkmdkfjd,mkdlkfnmdlfmkldj";


    public boolean validateToken(String token) { 		// kiểm tra token có đúng không
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public List<GrantedAuthority> extractAuthoritiesFromToken(String token) {
        // Giả sử sử dụng thư viện JWT để giải mã token
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(SECRET).build().parseClaimsJws(token);

        // Lấy danh sách quyền từ claims
        List<String> roles = claims.getBody().get("role", List.class);

        // Chuyển đổi danh sách quyền thành danh sách GrantedAuthority
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
