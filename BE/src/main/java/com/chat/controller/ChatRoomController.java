package com.chat.controller;

import com.chat.dto.request.RequestChatRoomDto;
import com.chat.dto.response.ResponseChatRoomDto;
import com.chat.dto.response.ResponseChatRoomListDto;
import com.chat.service.ChatRoomService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/create")
    public ResponseEntity<ResponseChatRoomDto> createChatRoom(@RequestBody @Valid RequestChatRoomDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(chatRoomService.createChatRoom(requestDto));
    }

    @GetMapping("/chatList")
    public ResponseEntity<ResponseChatRoomListDto> getChatRoomList() {

        return ResponseEntity.ok().body(chatRoomService.findChatRoomList());
    }
}
