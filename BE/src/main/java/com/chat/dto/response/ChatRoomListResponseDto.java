package com.chat.dto.response;

import com.chat.entity.ChatRoom;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomListResponseDto {
    private List<ChatRoomResponseDto> chatRoomList;

    public static ChatRoomListResponseDto from(List<ChatRoom> chatRooms) {
        return ChatRoomListResponseDto.builder()
            .chatRoomList(
                chatRooms.stream().map(
                    ChatRoomResponseDto::from
                ).toList()
            )
            .build();
    }
}
