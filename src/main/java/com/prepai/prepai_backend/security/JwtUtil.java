package com.prepai.prepai_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {

    @Value("${supabase.jwt.secret}")
    private String jwtSecret;

    // Extract all claims from token
    public Claims extractClaims(String token){
        SecretKey key = Keys.hmacShaKeyFor(
                jwtSecret.getBytes(StandardCharsets.UTF_8)
        );
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();


    }
    // Extract userId (subfield) from token
    public String extractUserId(String token){
        return extractClaims(token).getSubject();
    }

    //Check if token is valid
    public boolean isTokenValid(String token){
        try{
            extractClaims(token);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

}
