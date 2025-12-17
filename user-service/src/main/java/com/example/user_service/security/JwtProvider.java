package com.example.user_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    // Clé secrète stockée dans application.yml
    @Value("${JWT_SECRET_KEY}")
    private String jwtSecret;

    // Durée de validité du token (en ms, ici 24h)
    @Value("${JWT_EXPIRATION:86400000}")
    private long jwtExpiration;

    // ----------------------------------------------------
    // 1. GENERATION DU TOKEN
    // ----------------------------------------------------
    public String generateToken(String email, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(email) // L'utilisateur principal (email)
                .claim("role", role) // Les données additionnelles (rôle)
                .setIssuedAt(now) // Date de création
                .setExpiration(expiryDate) // Date d'expiration
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // ----------------------------------------------------
    // 2. RECUPERATION DE LA CLE SECRETE
    // ----------------------------------------------------
    private Key getSigningKey() {
        // La clé doit être au format base64
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ----------------------------------------------------
    // 3. VALIDATION (pour l'avenir)
    // ----------------------------------------------------
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Loguer l'erreur (Signature invalide, Token expiré, etc.)
            return false;
        }
    }

    // ----------------------------------------------------
    // 4. EXTRACTION (pour l'avenir)
    // ----------------------------------------------------
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}