import axios from "axios";
import React, { useEffect, useLayoutEffect, useRef, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { format, set } from "date-fns";

export default function ChatRoom() {
  const navigate = useNavigate();

  const token = localStorage.getItem("token");
  const loginedUsername = localStorage.getItem("username");
  const wsRef = useRef(null);
  const preMsgDate = useRef();

  // 채팅 스크롤
  const scrollRef = useRef(null);

  // 저장된 채팅 데이터
  const [preMsg, setPreMsg] = useState([]);

  // 실시간 채팅 데이터 처리
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");

  // useNavigate state에 보낸 값 가져오기
  // id: 채팅방 id  title: 채팅방 이름
  const location = useLocation();
  const { id, title } = location.state || {};

  // 화면 렌더링 전에 가장 최근 메세지로 포커스 이동
  useLayoutEffect(() => {
    scrollRef.current?.scrollIntoView({ behavior: "auto" });
  }, [preMsg]);

  useEffect(() => {
    if (!token) {
      navigate("/");
      return;
    }
    const getChatMessages = async () => {
      try {
        const response = await axios.get(
          import.meta.env.VITE_API_URL + `/find/chat/list/${id}`,
          { headers: { Authorization: "Bearer " + token } }
        );
        setPreMsg(response.data);
      } catch (error) {
        console.error(error);
      }
    };

    getChatMessages();
  }, [id, token]);

  // 웹소켓 연결 및 웹소켓 메세지 렌더링
  useEffect(() => {
    const ws = new WebSocket(
      `ws://${import.meta.env.VITE_WS_URL}/${id}?token=${token}`
    );
    wsRef.current = ws;

    ws.onmessage = (e) => {
      const messageData = JSON.parse(e.data);
      const { id, username, content } = messageData;
      const timestamp = format(new Date(id.date), "yyyy-MM-dd'T'HH:mm:ss");

      setMessages((prev) => [...prev, { username, content, timestamp }]);
    };

    ws.onclose = () => {
      console.warn("WebSocket closed");
    };

    return () => ws.close();
  }, [id, token]);

  useEffect(() => {
    //스크롤을 맨 아래로 이동
    scrollRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const sendMessage = () => {
    if (input.trim() && wsRef.current?.readyState === WebSocket.OPEN) {
      wsRef.current.send(input);
      setInput("");
    }
  };

  return (
    <>
      <div className="min-h-screen flex items-center justify-center ">
        <div className="max-w-2xl mx-auto mt-10 p-4 border rounded-lg shadow bg-white">
          <h2 className="text-2xl font-bold mb-4">{title}</h2>
          <div className="h-80 overflow-y-auto border p-3 rounded mb-4 bg-gray-50">
            {/* 이전에 보낸 채팅 기록들 가져오기 */}
            {preMsg.map((data, i) => {
              // 날짜 추출
              const currentDate = data.id.date.split("T")[0];
              // 다음 채팅메세지에서 이전 채팅메세지 날짜 저장(첫번째 메세지는 null 처리)
              const previousDate =
                i > 0 ? preMsg[i - 1].id.date.split("T")[0] : null;

              // 채팅메세지 현재 날짜랑 이전 채팅의 날짜가 다른 경우 판별하는 boolean 변수
              const isNewDate = currentDate !== previousDate;

              const loginedUser = data?.username == loginedUsername;

              if (isNewDate) {
                preMsgDate.current = currentDate;
              }
              return (
                <div key={`pre-${i}`} className="mb-2">
                  {isNewDate && (
                    <>
                      <div>{"\u00A0"}</div>
                      <div className="text-center text-gray-500 font-medium my-2">
                        {currentDate}
                        <hr />
                      </div>
                    </>
                  )}
                  <div
                    // 로그인한 사용자는 오른쪽 배치 & 초록 채팅박스 , 다른 사용자는 왼쪽 배치 & 회색 채팅박스
                    className={`border-2 ${
                      loginedUser ? "border-green-200" : "border-gray-600"
                    } rounded-xl p-2 flex flex-col ${
                      loginedUser ? "items-end" : "items-start"
                    }`}
                  >
                    <span className="font-semibold">
                      {data?.username ?? "익명"}
                    </span>
                    <div
                      className={`flex 
                        ${loginedUser ? "flex-row-reverse" : "flex-row"}
                      `}
                    >
                      <span>{data?.content ?? "내용 없음"}</span>
                      {"\u00A0"}
                      <span className="text-gray-500">
                        {data.id.date.split("T")[1].split(".")[0].slice(0, 5)}
                      </span>
                    </div>
                  </div>
                </div>
              );
            })}

            {/* 실시간 메시지 가져오기 */}
            {messages.map((msg, i) => {
              const currentDate = msg.timestamp?.split("T")[0];
              const previousDate =
                i > 0 ? messages[i - 1].timestamp?.split("T")[0] : null;
              const isNewDate = currentDate !== previousDate;
              const loginedUser = msg.username == loginedUsername;

              const isExistedDate = currentDate !== preMsgDate.current;

              return (
                <div key={`msg-${i}`} className="mb-2">
                  {isExistedDate && isNewDate && (
                    <div
                      ref={preMsgDate}
                      className="text-center text-gray-500 font-medium my-2"
                    >
                      {currentDate}
                    </div>
                  )}
                  <div
                    // 로그인한 사용자는 오른쪽 배치 & 초록 채팅박스 , 다른 사용자는 왼쪽 배치 & 회색 채팅박스
                    className={`border-2 ${
                      loginedUser ? "border-green-200" : "border-gray-600"
                    } rounded-xl p-2 flex flex-col ${
                      loginedUser ? "items-end" : "items-start"
                    }`}
                  >
                    <span className="font-semibold">
                      {msg?.username ?? "익명"}
                    </span>
                    <div
                      className={`flex 
                        ${loginedUser ? "flex-row-reverse" : "flex-row"}
                      `}
                    >
                      <span>{msg?.content ?? "내용 없음"}</span>
                      {"\u00A0"}
                      <span className="text-gray-500">
                        {msg.timestamp.split("T")[1].split(".")[0].slice(0, 5)}
                      </span>
                    </div>
                  </div>
                </div>
              );
            })}

            <div ref={scrollRef}></div>
          </div>
          <div className="flex items-center gap-2">
            <input
              className="flex-1 border rounded px-3 py-2"
              placeholder="메시지를 입력하세요..."
              value={input}
              onChange={(e) => setInput(e.target.value)}
              onKeyDown={(e) => e.key === "Enter" && sendMessage()}
            />
            <button
              className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
              onClick={sendMessage}
            >
              전송
            </button>
          </div>
        </div>
      </div>
    </>
  );
}
