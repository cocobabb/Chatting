package com.chat.controller;

import com.chat.dto.request.ChatRoomRequestDto;
import com.chat.dto.response.ChatRoomResponseDto;
import com.chat.dto.response.ChatRoomListResponseDto;
import com.chat.entity.User;
import com.chat.service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/create")
    public ResponseEntity<ChatRoomResponseDto> createChatRoom(@RequestBody @Valid ChatRoomRequestDto requestDto, @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(chatRoomService.createChatRoom(requestDto));
    }

    @GetMapping("/chatList")
    public ResponseEntity<ChatRoomListResponseDto> getChatRoomList(@RequestParam String username) {

        return ResponseEntity.ok().body(chatRoomService.findChatRoomList(username));
    }
}
