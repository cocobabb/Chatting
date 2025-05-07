package com.chat.global.security;

import io.jsonwebtoken.Claims;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();

        if(!jwtTokenProvider.validateToken(token)) {
            return Mono.empty();
        }

        Claims claims = jwtTokenProvider.getClaims(token);
        String username = claims.getSubject();
        List<GrantedAuthority> authorities = jwtTokenProvider.getAuthorities(claims);
        System.out.println(claims);
        System.out.println(username);
        System.out.println(authorities);
        return Mono.just(new UsernamePasswordAuthenticationToken(username,null, authorities));

    }
}
