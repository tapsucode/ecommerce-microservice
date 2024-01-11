package com.phamanh.productreviews.config;


import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;



@Component
public class JwtProvider {

	public String getUsernameFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(JwtConstant.SECRET_KEY).parseClaimsJws(token).getBody();
		return String.valueOf(claims.get("username"));
	}

	public Long getAccountIdFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(JwtConstant.SECRET_KEY).parseClaimsJws(token).getBody();
		return Long.parseLong(String.valueOf(claims.get("id"))) ;
	}
}
