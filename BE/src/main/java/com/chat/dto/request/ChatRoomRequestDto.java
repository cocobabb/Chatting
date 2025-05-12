package com.chat.dto.request;

import com.chat.entity.ChatRoom;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatRoomRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String username;

    @Builder
    public ChatRoom toEntity () {
        return ChatRoom.builder()
            .title(title)
            .build();
    }

}
