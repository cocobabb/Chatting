package com.chat.config;

import com.chat.global.security.CustomAccessDeniedHandler;
import com.chat.global.security.JwtAuthenticationEntryPoint;
import com.chat.global.security.JwtAuthenticationFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    @Value("${DEVELOP_FRONT_ADDRESS}")
    private String frontAddress;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return  new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

      http
          // Cross Origin Resource Sharing(웹브라우저에서 보안상의 이유로 프로토콜, 도메인, 포트 하나만 달라도 다른 출처로 인식해 요청 제한) 허용 origin 설정
          .cors(cors -> cors.configurationSource(corsConfigurationSource()))
          // Cross-Site Request Forgery 공격 방지를 위해 비활성화
          .csrf(csrf -> csrf.disable())
          .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .authorizeHttpRequests(auth -> auth
              .requestMatchers("/auth/**").permitAll()
              .requestMatchers("/error").permitAll()
              .requestMatchers("/images/**").permitAll()
              .anyRequest().authenticated()
          )
          .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
          .exceptionHandling(exception -> exception
              .accessDeniedHandler(accessDeniedHandler)
              .authenticationEntryPoint(jwtAuthenticationEntryPoint)
          );
      return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of(frontAddress)); // addAllowedOriginPattern은 와일드카드용
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        // 쿠키 전달을 위한 설정
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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
