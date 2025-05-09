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
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

        // 권한(authorities)을 문자열 리스트로 변환 [ADMIN, USER]
        List<String> roles = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());


        claims.put("roles", roles);


        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInMillis);

        return Jwts.builder()
            .setClaims(claims) // payload 설정
            .setIssuedAt(now) // 발급 시간
            .setExpiration(validity) // 만료 시간
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
            .compact(); // JWT 문자열 형식으로 변환
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
            System.out.println("ValidateToken exception 발생");
            System.out.printf("JWT validation failed: %s", e.getMessage());
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



    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    @SuppressWarnings("unchecked")
    public List<GrantedAuthority> getAuthorities(Claims claims) {
        List<String> roles = (List<String>) claims.get("roles");
        return roles.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toUnmodifiableList());
    }



}
