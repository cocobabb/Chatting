package com.chat.dto;

import jakarta.persistence.Id;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chatting_content") // 실제 몽고 DB 컬렉션 이름
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    @Id
    private ObjectId id;
    private Long roomId;
    private String username;
    private String content;
    private Long writerId;


    public static ChatMessage from(ChatMessage chatMessage) {
        return ChatMessage.builder()
            .roomId(chatMessage.getRoomId())
            .username(chatMessage.getUsername())
            .content(chatMessage.getContent())
            .build();
    }
}

