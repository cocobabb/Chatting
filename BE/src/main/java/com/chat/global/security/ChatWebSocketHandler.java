package com.chat.global.security;

import com.chat.dto.ChatMessage;
import com.chat.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler implements WebSocketHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ChatService chatService;
    private final ChatSessionManager sessionManager;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        System.out.println("ChatWebSocketHandler Access");
        String uri = session.getHandshakeInfo().getUri().toString();
        String token = UriComponentsBuilder.fromUriString(uri).build().getQueryParams().getFirst("token");
        String path = session.getHandshakeInfo().getUri().getPath(); // 예: /ws/chat/room123
        String roomId = path.substring(path.lastIndexOf("/") + 1);

        // 인증 실패 시 연결 종료
        if(!jwtTokenProvider.validateToken(token)) {
            System.out.println("유효하지 않은 토큰임");
            return session.close();
        }

        String username  = jwtTokenProvider.getUsername(token);

        sessionManager.register(roomId, session); // 연결된 세션 등록

        // 메시지 수신 처리
        return session.receive()
            .map(WebSocketMessage::getPayloadAsText)
            .flatMap(message -> {
                // MongoDB에 채팅 메세지 저장
                ChatMessage chatMessage = ChatMessage.builder()
                    .id(new ObjectId())
                    .roomId(Long.valueOf(roomId))
                    .username(username)
                    .content(message)
                    .build();
                Mono<Void> saveTask = chatService.saveChatMessage(chatMessage);
                // 브로드캐스트
                Mono<Void> broadcastTask = broadcastToRoom(roomId, chatMessage);

                return saveTask.then(broadcastTask);
            })
            .then();
    }

    private Mono<Void> broadcastToRoom(String roomId, ChatMessage chatMessage) {
        return Flux.fromIterable(sessionManager.getSession(roomId))
            .filter(WebSocketSession::isOpen)
            .flatMap(session ->
                {
                    try {
                        // ChatMessage를 JSON으로 직렬화
                        // WebSocketSession.send()는 웹소켓 메세지만 받을 수 있으며 그 안의 데이터는 문자열 또는 바이트 바이너리여야 함
                        String jsonPayload = objectMapper.writeValueAsString(chatMessage);
                        Mono<WebSocketMessage> messageMono = Mono.just(session.textMessage(jsonPayload));
                        return  session.send(messageMono);

                    }catch (Exception e){
                        System.out.println(e.getMessage());
                        return Mono.empty();
                    }
                }
            )
            .then();
    }


}
