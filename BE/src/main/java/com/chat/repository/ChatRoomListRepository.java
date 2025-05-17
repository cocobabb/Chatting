package com.chat.repository;

import com.chat.entity.ChatRoom;
import com.chat.entity.ChatRoomList;
import com.chat.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomListRepository extends JpaRepository<ChatRoomList, String> {

    List<ChatRoomList> findByUser(User user);
}
