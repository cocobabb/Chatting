package com.chat.controller;

import com.chat.dto.request.LoginRequestDto;
import com.chat.dto.request.SignupRequestDto;
import com.chat.dto.response.SignupResponseDto;
import com.chat.dto.response.TokenResponseDto;
import com.chat.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto)
        throws IllegalAccessException {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body((authService.signup(requestDto)));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(
        @Valid @RequestBody LoginRequestDto requestDto
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body((authService.login(requestDto)));
    }
}
