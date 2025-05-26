import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import ChatRoom from "./ChatRoom";

export default function ChatRoomList() {
  const token = localStorage.getItem("token");
  const username = localStorage.getItem("username");

  const [chatRooms, setChatRooms] = useState([]);
  const navigate = useNavigate();

  const [input, setInput] = useState("");

  useEffect(() => {
    if (!token) {
      navigate("/");
      return;
    }
    const getChatRoomList = async () => {
      try {
        const response = await axios.get(
          import.meta.env.VITE_API_URL + `/chatList?username=${username}`,
          { headers: { Authorization: "Bearer " + token } }
        );
        const chatRoomList = response.data.chatRooms;
        console.log(chatRoomList);
        setChatRooms(chatRoomList);
      } catch (error) {
        console.error(error);
      }
    };

    getChatRoomList();
  }, []);

  // 입력 값 제어
  const handleInput = (e) => {
    const chatRoomTitle = e.target.value;
    if (chatRoomTitle.trim() === "" && chatRoomTitle !== "") return;

    setInput(chatRoomTitle);
  };

  const makeChatRoom = async () => {
    console.log("채팅룸 만들기 입장");
    if (!input) return;
    console.log("채팅룸 만들기 조건문 이후");
    try {
      const response = await axios.post(
        import.meta.env.VITE_API_URL + "/create",
        { title: input, username },
        { headers: { Authorization: "Bearer " + token } }
      );
      setChatRooms([...chatRooms, response.data]);
      setInput(""); // 입력창 초기화
    } catch (error) {
      console.error(error);
    }
  };

  function moveToChatRoom(id, title) {
    title = title.replaceAll(" ", "");
    navigate(`/chat-rooms/${id}`, {
      state: { id, title },
    });

    return;
  }

  return (
    <>
      <div className="text-3xl font-bold text-center mt-10 text-green-700">
        채팅방 목록
      </div>

      <div className="flex justify-center m-5">
        <input
          className="w-80 border rounded px-3 py-2"
          placeholder="채팅방 이름을 입력하세요..."
          value={input}
          onChange={handleInput}
          onKeyDown={(e) => e.key === "Enter" && makeChatRoom()}
        />
        <button
          className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
          onClick={makeChatRoom}
        >
          채팅방 생성
        </button>
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
