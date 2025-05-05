package com.chat.dto.request;

import com.chat.entity.Role;
import com.chat.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {
    @NotBlank(message = "아이디는 필수 입력값입니다")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력값입니다")
//    ^ : 문자열의 시작
//    (?=.*[A-Za-z]) : 하나 이상의 영문자 (대소문자 포함) 반드시 포함
//    (?=.*\\d) : 하나 이상의 숫자 반드시 포함
//    (?=.*[@$!%*#?&]) : 하나 이상의 특수문자 반드시 포함 (지정된 특수문자 집합 내에서)
//    [A-Za-z\\d@$!%*#?&]{8,} : 위에 포함된 문자들로만 이루어져야 하며, 최소 8자 이상
//    $ : 문자열의 끝
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&*])[a-z\\d@#$%^&*]{6,}$",
        message = "비밀번호는 영문, 숫자, 특수문자 포함 6글자 이상이여야 합니다."
    )
    private String password;

    @NotBlank(message = "국가는 필수 입력값입니다")
    private String country;

    public User toEntity(String encodedPassword) {
        return User.builder()
            .username(this.getUsername())
            .password(encodedPassword)
            .country(this.getCountry())
            .role(Role.ROLE_USER)
            .build();
    }



}
