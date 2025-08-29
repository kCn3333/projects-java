package com.kcn.orders_api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Service
public class JwtServiceImpl implements JwtService{

    private final Key SECRET_KEY;
    private final long JWT_EXPIRATION;

    public JwtServiceImpl(@Value("${spring.jwt.secret}") String secret,
                          @Value("${spring.jwt.expiration}") long expiration) {
        // dekodujemy Base64 i tworzymy Key dla HS256
        this.SECRET_KEY = Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(secret));
        this.JWT_EXPIRATION = expiration;
    }


    @Override
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            Date expiration = claims.getExpiration();
            boolean notExpired = expiration.after(new Date());

            return username.equals(userDetails.getUsername()) && notExpired;
        } catch (JwtException e) {
            return false;
        }
    }

    @Override
    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(SECRET_KEY)
                .compact();
    }

    public Set<String> extractRoles(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("roles", Set.class);
    }
}
