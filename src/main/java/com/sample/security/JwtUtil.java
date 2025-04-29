package com.sample.security;

import com.sample.dto.AuthResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Expiration times for access and refresh tokens
    private static final long ACCESS_TOKEN_EXPIRATION_TIME_MS = 1000 * 60 * 15; // 15 minutes
    private static final long REFRESH_TOKEN_EXPIRATION_TIME_MS = 1000 * 60 * 60 * 24 * 7; // 7 days

    private final Key key;

    public JwtUtil(Key key) {
        this.key = key;
    }

    public String generateAccessToken(String username) {
        return generateToken(username, ACCESS_TOKEN_EXPIRATION_TIME_MS);
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, REFRESH_TOKEN_EXPIRATION_TIME_MS);
    }

    private String generateToken(String username, long expirationTime) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
