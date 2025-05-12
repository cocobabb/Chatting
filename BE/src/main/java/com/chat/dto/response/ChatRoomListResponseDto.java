package com.chat.dto.response;

import com.chat.entity.ChatRoom;
import com.chat.entity.ChatRoomList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomListResponseDto {
    private List<ChatRoomResponseDto> chatRooms;

    public static ChatRoomListResponseDto from(List<ChatRoomList> chatRoomLists) {
        return ChatRoomListResponseDto.builder()
            .chatRooms(
                chatRoomLists.stream().map(
                    ChatRoomList::getChatRoom
                ).toList().stream().map(ChatRoomResponseDto::from).toList()
            )
            .build();
    }
}
