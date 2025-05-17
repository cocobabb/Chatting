package com.chat.controller;

import com.chat.dto.ChatMessage;
import com.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;


    /**
     * 채팅내용 리스트 반환
     * @param id 채팅방 id
     * **/
    @GetMapping("/find/chat/list/{id}")
    public Flux<ChatMessage> find(@PathVariable("id") String id) {
        return chatService.findChatMessageList(id);
    }

    /**
     * 메시지 송신 및 수신
     * **/
//    @MessageMapping("/message")
//   public void receiveMessage(ChatMessage ChatMessage) {

//    public type saveMessage(ChatMessage chatMessage){
//        System.out.println("메세지 저장하는 컨트롤러 진입");
//        chatService.saveChatMessage(chatMessage);
//    }


}
