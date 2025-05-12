package com.chat.service;

import com.chat.dto.request.ChatRoomRequestDto;
import com.chat.dto.response.ChatRoomResponseDto;
import com.chat.dto.response.ChatRoomListResponseDto;
import com.chat.entity.ChatRoom;
import com.chat.entity.ChatRoomList;
import com.chat.entity.User;
import com.chat.repository.ChatRoomListRepository;
import com.chat.repository.ChatRoomRepository;
import com.chat.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomListRepository chatRoomListRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChatRoomResponseDto createChatRoom(ChatRoomRequestDto requestDto) {
        ChatRoom chatRoom = requestDto.toEntity();
        chatRoomRepository.save(chatRoom);

        User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow();
        ChatRoomList chatRoomList = ChatRoomList.builder()
            .chatRoom(chatRoom)
            .user(user)
            .build();
        chatRoomListRepository.save(chatRoomList);

        return ChatRoomResponseDto.from(chatRoom);
    }

    public ChatRoomListResponseDto findChatRoomList(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        List<ChatRoomList> chatRoomLists = chatRoomListRepository.findByUser(user);

        return ChatRoomListResponseDto.from(chatRoomLists);
    }
}
