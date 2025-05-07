package com.chat.config;

import com.chat.global.security.JwtAuthenticationManager;
import com.chat.global.security.JwtSecurityContextRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    @Value("${DEVELOP_FRONT_ADDRESS}")
    private String frontAddress;

    private final JwtAuthenticationManager jwtAuthenticationManager;
    private final JwtSecurityContextRepository jwtSecurityContextRepository;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {

        http
            // Cross-Site Request Forgery 공격 방지를 위해 비활성화
            .csrf(csrf -> csrf.disable())
            // Cross Origin Resource Sharing(웹브라우저에서 보안상의 이유로 프로토콜, 도메인, 포트 하나만 달라도 다른 출처로 인식해 요청 제한) 허용 origin 설정
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            .securityContextRepository(jwtSecurityContextRepository)
            .authenticationManager(jwtAuthenticationManager)
            .authorizeExchange(auth -> auth
                // WebSocket 연결은 토큰으로 직접 인증하므로 허용
                .pathMatchers("/auth/**", "/ws/**", "/error", "images/**").permitAll()
                .anyExchange().authenticated()
            );
        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(frontAddress));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        // 쿠키 전달을 위한 설정
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    
///////////////////////////////// 사용자 관련 //////////////////////////////////
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        // 사용자의 정보를 조회하는 인터페이스
        UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder
    ) {
        // DaoAuthenticationProvider : 사용자 인증을 위한 클래스
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }


}
