package com.chat.dto.response;

import com.chat.entity.ChatRoom;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseChatRoomDto {
    private final Long id;
    private final String title;

    public static ResponseChatRoomDto from(ChatRoom chatRoom) {
        return ResponseChatRoomDto.builder()
            .id(chatRoom.getId())
            .title(chatRoom.getTitle())
            .build();
    }
}
