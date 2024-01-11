package com.phamanh.accountservice.security;


import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtProvider {
	private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

	
	// ham tạo token
	public String createToken(Authentication authentication) {
		
		UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal(); // lấy tài khoản từ bên trong authen
		
		return Jwts.builder().setSubject(userPrinciple.getUsername())
				.setIssuedAt(new Date()) // ngày khởi tạo
				.setExpiration(new Date(new Date().getTime()+JwtConstant.JWT_EXPIRATION)) // thời gian sống
				.signWith(SignatureAlgorithm.HS512, JwtConstant.SECRET_KEY) // kiểu mã hóa và key dữ liệu
				.compact();
	}
	
	public boolean validateToken(String token) { 		// kiểm tra token có đúng không
		try {
            Jwts.parser().setSigningKey(JwtConstant.SECRET_KEY).parseClaimsJws(token);
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
	
	
	// lấy username thông qua token
	
	public String getUsernameFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(JwtConstant.SECRET_KEY).parseClaimsJws(token).getBody();
		return String.valueOf(claims.get("username"));
	}

	public Long getAccountIdFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(JwtConstant.SECRET_KEY).parseClaimsJws(token).getBody();
		return Long.parseLong(String.valueOf(claims.get("id"))) ;
	}
}
