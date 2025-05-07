package com.chat.service;

import com.chat.dto.ChatMessage;
import com.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRepository chatRepository;
//    private final SimpMessageSendingOperations template;

    /**
     * 특정 채팅방에 저장된 메세지 리스트 가져오기
     * **/
    public Flux<ChatMessage> findChatMessageList(Long id) {
        Flux<ChatMessage> chatMessages = chatRepository.findAllByRoomId(id);
        return chatMessages;
    }

    /**
     * 메세지 저장  ChatWebsocketHandler 에서 진행
     * **/
    @Transactional
    public Mono<Void> saveChatMessage(ChatMessage message) {
        System.out.println(">>> 저장 요청 받은 메세지: " + message.getContent());

        // 메세지 저장
        Mono<ChatMessage> chatMessageMono = chatRepository.save(
            ChatMessage.from(message));

        System.out.println(chatMessageMono);

        return null;
    }
}
