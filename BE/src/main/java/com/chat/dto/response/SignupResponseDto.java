package com.chat.dto.response;

import com.chat.entity.Role;
import com.chat.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponseDto {
    private String username;
    private String country;
    private Role role;

    public static SignupResponseDto from(User entity){
        return SignupResponseDto.builder()
            .username(entity.getUsername())
            .country(entity.getCountry())
            .role(entity.getRole())
            .build();
    }
}
