package com.chat.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private final long tokenValidityInMillis = 1000L * 60 * 60; // 1시간

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(Authentication authentication) {
        String username = authentication.getName();
        Claims claims = Jwts.claims().setSubject(username);

        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInMillis);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
            .compact();
    }

    // 토큰 위조 확인
    public boolean validateToken(String token) {
        // 토큰 서명을 검증하고 본문 디코딩
        // 토큰이 변조되거나 만료되었다면 예외 던짐
        try {
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token);
            return true;
        }catch (JwtException | IllegalArgumentException e) {
            return false;
        }
        
    }
    
    public String getUsername(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody()
            // 본문 가져와 subject에 할당한 username 가져오기
            .getSubject();
    }
}
