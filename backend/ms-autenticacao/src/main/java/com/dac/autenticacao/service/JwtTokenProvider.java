package com.dac.autenticacao.service;

import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm; // <-- REMOVIDO
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
// import java.util.Map; // (Não estava a ser usado)

@Service
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecretBase64;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    private SecretKey getSecretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(jwtSecretBase64);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    // Gera um novo token
    public String generateToken(String email, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSecretKey())
                .compact();
    }

}