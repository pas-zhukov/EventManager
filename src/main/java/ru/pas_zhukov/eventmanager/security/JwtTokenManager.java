package ru.pas_zhukov.eventmanager.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenManager {

    private final SecretKey key;
    private final long expirationTime;

    public JwtTokenManager(
            @Value("${jwt.secret-key}") String keyString,
            @Value("${jwt.lifetime}") long expirationTime
    ) {
        this.key = Keys.hmacShaKeyFor(keyString.getBytes());
        this.expirationTime = expirationTime;
    }

    public String generateToken(String login) {
        return Jwts
                .builder()
                .subject(login)
                .claim("authority", "ADMIN")
                .signWith(key)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .compact();
    }

    public String getLoginFromToken(String jwtToken) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload()
                .getSubject();
    }
}