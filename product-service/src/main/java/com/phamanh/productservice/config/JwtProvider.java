package com.phamanh.productservice.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtProvider {
	private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

	
	// ham tạo token

	public static Long getAccountIdFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(JwtConstant.SECRET_KEY).parseClaimsJws(token).getBody();
		return Long.parseLong(String.valueOf(claims.get("id"))) ;
	}
}
