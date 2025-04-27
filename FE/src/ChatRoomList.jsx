import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";

const ChatRoomList = () => {
  const [chatRoomList, setChatRoomList] = useState([]);
  const [inputValue, setInputValue] = useState("");
  // 입력 필드에 변화가 있을 때마다 inputValue를 업데이트
  const handleInputChange = (event) => {
    setInputValue(event.target.value);
  };

  const fetchRooms = () => {
    axios.get("http://localhost:8080/chatList").then((response) => {
      console.log(response.data);
      setChatRoomList(response.data.chatRoomList);
    });
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter") {
      createRoom();
    }
  };

  const createRoom = () => {
    if (inputValue) {
      const body = {
        title: inputValue,
      };
      axios.post("http://localhost:8080/create", body).then((response) => {
        console.log(response);
        if (response.status === 201) {
          setInputValue("");
          setChatRoomList((prev) => [...prev, response.data]);
        } else {
          alert("경고경고!");
        }
      });
    }
  };

  useEffect(() => {
    fetchRooms();
  }, []);
  return (
    <div>
      <ul>
        <div>
          {/* 입력 필드 */}
          <input
            type="text"
            value={inputValue}
            onChange={handleInputChange}
            onKeyDown={handleKeyDown}
          />
          {/* 채팅방 추가 */}
          <button onClick={createRoom}>입력</button>
        </div>
        {/* 채팅방 리스트 출력 */}
        {chatRoomList.map((chatRoom, index) => (
          <Link key={index} to={`/chatroom/${chatRoom.id}`}>
            <div>{chatRoom.title}</div>
          </Link>
        ))}
      </ul>
    </div>
  );
};
export default ChatRoomList;
