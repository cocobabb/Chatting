package com.chat.dto.response;

import com.chat.entity.ChatRoom;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseChatRoomListDto {
    private List<ResponseChatRoomDto> chatRoomList;

    public static ResponseChatRoomListDto from(List<ChatRoom> chatRooms) {
        return ResponseChatRoomListDto.builder()
            .chatRoomList(
                chatRooms.stream().map(
                    ResponseChatRoomDto::from
                ).toList()
            )
            .build();
    }
}
