import axios from "axios";
import React, { useEffect, useRef, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

export default function ChatRoom() {
  const navigate = useNavigate();

  const token = localStorage.getItem("token");
  const loginedUsername = localStorage.getItem("username");
  const wsRef = useRef(null);

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
        console.log(response);
        setPreMsg(response.data);
        // 스크롤을 맨 아래로 이동
        scrollRef.current?.scrollIntoView({ behavior: "smooth" });
      } catch (error) {
        console.error(error);
      }
    };

    getChatMessages();
  }, [id, token]);

  // 웹소켓 연결 및 메세지 렌더링
  useEffect(() => {
    console.log(id);

    const ws = new WebSocket(
      `ws://${import.meta.env.VITE_WS_URL}/${id}?token=${token}`
    );
    wsRef.current = ws;

    ws.onmessage = (e) => {
      console.log("on");
      console.log(e.data);
      const [sender, ...rest] = e.data.split(":"); // split은 :을 기준으로 : 앞 요소는 변수로 : 뒤 요소는 배열로 만듦
      const text = rest.join(""); // 배열에 ""문자열을 더해서 문자열로 만듦

      console.log("sender:", sender);
      console.log("text:", text);
      setMessages((prev) => [...prev, { sender, text }]);
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
            {preMsg.map((data, i) => {
              // 날짜 추출
              const currentDate = data.id.date.split("T")[0];
              // 다음 채팅메세지에서 이전 채팅메세지 날짜 저장(첫번째 메세지는 null 처리)
              const previousDate =
                i > 0 ? preMsg[i - 1].id.date.split("T")[0] : null;

              // 채팅메세지 현재 날짜랑 이전 채팅의 날짜가 다른 경우 판별하는 boolean 변수
              const isNewDate = currentDate !== previousDate;

              const loginedUser = data?.username == loginedUsername;

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

            {messages.map((msg, i) => (
              <div key={i} className="mb-2">
                <span className="font-semibold">{msg?.sender ?? "익명"}</span>:
                {msg?.text ?? "텍스트 없음"}
              </div>
            ))}

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
