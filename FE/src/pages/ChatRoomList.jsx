import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import ChatRoom from "./ChatRoom";

export default function ChatRoomList() {
  const [chatRooms, setChatRooms] = useState([]);
  const navigate = useNavigate();
  useEffect(() => {
    const getChatRoomList = async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        navigate("/");
        return;
      }
      try {
        const response = await axios.get(
          import.meta.env.VITE_API_URL + "/chatList",
          { headers: { Authorization: "Bearer " + token } }
        );
        const chatRoomList = response.data.chatRoomList;
        console.log(chatRoomList);
        setChatRooms(chatRoomList);
      } catch (error) {
        console.error(error);
      }
    };

    getChatRoomList();
  }, []);

  function moveToChatRoom(id, title) {
    title = title.replaceAll(" ", "");
    // encodeURIComponent(String) : 어떤 언어든 안전하게 URL로 변환(URL에서 title 꺠지는 거 방지)
    navigate(`/chat-rooms/${encodeURIComponent(title)}`, {
      state: { id, title },
    });

    return;
  }

  return (
    <>
      <div className="text-3xl font-bold text-center mt-10 text-green-700">
        채팅방 목록
      </div>
      <div className="w-full h-screen flex justify-center">
        <div className="w-full max-w-md space-y-4 mt-8">
          {chatRooms.map((room) => (
            <div
              onClick={() => moveToChatRoom(room.id, room.title)}
              key={room.id}
              className="cursor-pointer p-4 bg-white rounded-2xl shadow hover:bg-green-100 transition duration-200"
            >
              {room.title}
            </div>
          ))}
        </div>
      </div>
    </>
  );
}
