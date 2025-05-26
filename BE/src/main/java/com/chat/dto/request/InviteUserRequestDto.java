package com.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class InviteUserRequestDto {

    @NotBlank
    String chatRoomId;

}
