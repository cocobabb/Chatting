package com.chat.service;

import com.chat.dto.request.ChatRoomRequestDto;
import com.chat.dto.response.ChatRoomResponseDto;
import com.chat.dto.response.ChatRoomListResponseDto;
import com.chat.entity.ChatRoom;
import com.chat.repository.ChatRoomRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatRoomResponseDto createChatRoom(ChatRoomRequestDto requestDto) {
        ChatRoom chatRoom = requestDto.toEntity();
        chatRoomRepository.save(chatRoom);

        return ChatRoomResponseDto.from(chatRoom);
    }

    public ChatRoomListResponseDto findChatRoomList() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();

        return ChatRoomListResponseDto.from(chatRooms);
    }
}
