package com.chat.global.security;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;

@Component
public class ChatSessionManager {

    private final Map<String, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    public void register(String roomId, WebSocketSession session) {
        roomSessions.computeIfAbsent(roomId,  key -> ConcurrentHashMap.newKeySet()).add(session);
    }

    public void unregister(String roomId, WebSocketSession session) {
        Set<WebSocketSession> sessions = roomSessions.get(roomId);

        if(sessions != null) {
            sessions.remove(session);
        }
    }

    public Set<WebSocketSession> getSession(String roomId) {
        return roomSessions.getOrDefault(roomId, Set.of());
    }

}
