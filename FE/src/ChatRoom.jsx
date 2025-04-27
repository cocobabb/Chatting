import axios from "axios";
import React, { useEffect, useState, useRef } from "react";
import { Stomp } from "@stomp/stompjs";
import { useParams } from "react-router-dom";

const ChatRoom = () => {
  const [selectedNumber, setSelectedNumber] = useState([]);

  const numbers = [1, 2, 3, 4, 5]; // 회원 번호를 위한 숫자 배열

  const handleNumberClick = (number) => {
    setSelectedNumber(number);
  };

  const { chatRoomId } = useParams();

  const stompClient = useRef(null);
  // 채팅 내용들을 저장할 변수
  const [messages, setMessages] = new useState([]);
  // 사용자 입력을 저장할 변수
  const [inputValue, setInputValue] = useState("");
  // 입력 필드에 변화가 있을 때마다 inputValue를 업데이트
  const handleInputChange = (event) => {
    setInputValue(event.target.value);
  };
  // 웹소켓 연결 설정
  const connect = () => {
    const socket = new WebSocket("ws://localhost:8080/ws-stomp");
    stompClient.current = Stomp.over(socket);
    stompClient.current.connect({}, () => {
      stompClient.current.subscribe(
        `/sub/chatroom/` + chatRoomId,
        (message) => {
          const newMessage = JSON.parse(message.body);
          setMessages((prevMessages) => [...prevMessages, newMessage]);
        }
      );
    });
  };
  // 웹소켓 연결 해제
  const disconnect = () => {
    if (stompClient.current) {
      stompClient.current.disconnect();
    }
  };
  // 기존 채팅 메시지를 서버로부터 가져오는 함수
  const fetchMessages = () => {
    axios
      .get("http://localhost:8080/find/chat/list/" + chatRoomId)
      .then((response) => {
        console.log(response);
        setMessages(response.data);
      });
  };
  useEffect(() => {
    connect();
    fetchMessages();
    // 컴포넌트 언마운트 시 웹소켓 연결 해제
    return () => disconnect();
  }, []);

  const handleKeyDown = (e) => {
    if (e.key === "Enter") {
      sendMessage();
    }
  };

  //메세지 전송
  const sendMessage = () => {
    if (stompClient.current && inputValue && selectedNumber) {
      const body = {
        roomId: chatRoomId,
        content: inputValue,
        writerId: selectedNumber,
      };
      stompClient.current.send(`/pub/message`, {}, JSON.stringify(body));
      setInputValue("");
    }
  };

  return (
    <div>
      <ul>
        <div style={{ display: "flex" }}>
          {numbers.map((number, index) => (
            <div
              key={index}
              onClick={() => handleNumberClick(number)}
              style={{
                marginRight: "5px",
                padding: "5px",
                width: "40px",
                height: "25px",
                border: "1px solid black",
                borderRadius: "5px",
                textAlign: "center",
              }}
            >
              {number}
            </div>
          ))}
          <p style={{ marginTop: "7px" }}>회원 번호: {selectedNumber}</p>
        </div>
        <div>
          {/* 입력 필드 */}
          <input
            type="text"
            value={inputValue}
            onChange={handleInputChange}
            onKeyDown={handleKeyDown}
          />
          {/* 메시지 전송, 메시지 리스트에 추가 */}
          <button onClick={sendMessage}>입력</button>
        </div>
        {/* 메시지 리스트 출력 */}
        {messages.map((chatting, index) => (
          <div key={index}>
            {chatting.writerId}: {chatting.content}
          </div>
        ))}
      </ul>
    </div>
  );
};
export default ChatRoom;
