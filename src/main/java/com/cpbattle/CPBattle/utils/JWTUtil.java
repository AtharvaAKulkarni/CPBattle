package com.cpbattle.CPBattle.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {
    @Value("${jwtSecret}")
    private String SECRET;
    public String generateToken(String username) {
        Map<String, Object> claims=new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Date extractExpiration(String token){
        return extractAllClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, String username){
        final String extractedUsername=extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }
}
