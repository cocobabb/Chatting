package com.chat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "chat_room_List")
@NoArgsConstructor
public class ChatRoomList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "chatRoom_id", nullable = false)
    private ChatRoom chatRoom;

    @Builder
    public ChatRoomList(User user, ChatRoom chatRoom) {
        this.user = user;
        this.chatRoom = chatRoom;
    }



}
