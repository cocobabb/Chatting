package com.chat.service;

import com.chat.dto.request.LoginRequestDto;
import com.chat.dto.request.SignupRequestDto;
import com.chat.dto.response.SignupResponseDto;
import com.chat.dto.response.TokenResponseDto;
import com.chat.entity.User;
import com.chat.global.security.JwtTokenProvider;
import com.chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public SignupResponseDto signup(SignupRequestDto requestDto) throws IllegalAccessException {
        if(userRepository.existsByUsername(requestDto.getUsername())) {
            throw new IllegalAccessException("이미 사용 중인 아이디입니다.");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        User user = requestDto.toEntity(encodedPassword);

        return SignupResponseDto.from(userRepository.save(user));

    }

    public TokenResponseDto login(LoginRequestDto requestDto) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                requestDto.getUsername(),
                requestDto.getPassword()
            )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.createToken(authentication);

        return new TokenResponseDto(jwt);
    }
}
