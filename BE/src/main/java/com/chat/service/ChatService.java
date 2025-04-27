package com.chat.service;

import com.chat.dto.ChatMessage;
import com.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRepository chatRepository;
    private final SimpMessageSendingOperations template;


    public Flux<ChatMessage> findChatMessageList(Long id) {
        Flux<ChatMessage> chatMessages = chatRepository.findAllByRoomId(id);
        return chatMessages;
    }

    @Transactional
    public void saveChatMessageAndSend(ChatMessage message) {
        System.out.println(">>> 저장 요청 받은 메세지: " + message.getContent());

        // 메세지 저장
        Mono<ChatMessage> chatMessageMono = chatRepository.save(
            ChatMessage.from(message));

        System.out.println(chatMessageMono);

        // 메세지를 해당 채팅방 구독자들에게 메세지 전달
            chatMessageMono.doOnNext(chatMessage -> {
                System.out.println(">>> MongoDB 저장 성공한 메세지: " + chatMessage.getContent());
                template.convertAndSend(
                    "/sub/chatroom/" + message.getRoomId(),
                    ChatMessage.from(message)
                );
            }).subscribe();
    }
}
