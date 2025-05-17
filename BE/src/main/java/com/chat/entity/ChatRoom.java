package com.chat.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.chat.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "chat_room")
@NoArgsConstructor
public class ChatRoom extends BaseTimeEntity {

    @Id
    @Column(name = "id", unique = true, updatable = false, nullable = false)
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoomList> chatRoomLists = new ArrayList<>();


    @Builder
    public ChatRoom(String title) {
        this.id = NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, 8);
        this.title = title;
    }



}