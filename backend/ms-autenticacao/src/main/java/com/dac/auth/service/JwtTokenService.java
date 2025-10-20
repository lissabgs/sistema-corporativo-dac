package com.dac.auth.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    @Value("${app.security.jwt.secret-key}")
    private String jwtSecret;

    @Value("${app.security.jwt.expiration-hours}")
    private Integer expirationHours;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    private Long getExpirationMs() {
        return expirationHours * 60 * 60 * 1000L;
    }

    public String generateToken(String email, String tipoUsuario) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + getExpirationMs());

        return Jwts.builder()
                .setSubject(email)
                .claim("tipoUsuario", tipoUsuario)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //✅ MÉTODO PARA AuthenticationManager (igual professor
    public String generateToken(Authentication authentication) {
        String email = authentication.getName();
        String tipoUsuario = authentication.getAuthorities().iterator().next().getAuthority();
        return generateToken(email, tipoUsuario);
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public String getTipoUsuarioFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("tipoUsuario", String.class);
    }

    public Date getExpirationFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration();
    }

    //MÉTODO ALTERNATivo - retorna String formatada
    public String getExpirationAsString(String token) {
        Date expiration = getExpirationFromToken(token);
        return expiration.toString();
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}