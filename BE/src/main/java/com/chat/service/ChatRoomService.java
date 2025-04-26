package com.chat.service;

import com.chat.dto.request.RequestChatRoomDto;
import com.chat.dto.response.ResponseChatRoomDto;
import com.chat.dto.response.ResponseChatRoomListDto;
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
    public ResponseChatRoomDto createChatRoom(RequestChatRoomDto requestDto) {
        ChatRoom chatRoom = requestDto.toEntity();
        chatRoomRepository.save(chatRoom);

        return ResponseChatRoomDto.from(chatRoom);
    }

    public ResponseChatRoomListDto findChatRoomList() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();

        return ResponseChatRoomListDto.from(chatRooms);
    }
}
