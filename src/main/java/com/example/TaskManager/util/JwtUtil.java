package com.example.TaskManager.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private String secret = "mysecretkeymysecretkeymysecretkey";

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }
}
