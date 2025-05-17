package com.chat.repository;

import com.chat.dto.ChatMessage;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.core.publisher.Flux;

@EnableReactiveMongoRepositories
public interface ChatRepository extends ReactiveMongoRepository<ChatMessage, ObjectId> {
    Flux<ChatMessage> findAllByRoomId(String id);
}
