import axios from "axios";
import React, { useEffect, useRef, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

export default function ChatRoom() {
  const navigate = useNavigate();

  const token = localStorage.getItem("token");
  const wsRef = useRef(null);

  const [preMsg, setPreMsg] = useState([]);
  const [date, setDate] = useState(new Set());

  const [messages, setMessages] = useState([]);
  const scrollRef = useRef(null);
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
      const response = await axios.get(
        import.meta.env.VITE_API_URL + `/find/chat/list/${id}`,
        { headers: { Authorization: "Bearer " + token } }
      );
      console.log(response);
      setPreMsg(response.data);
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
            <div>{date}</div>

            {preMsg.map((data, i) => (
              <div key={i} className="mb-2">
                <span className="font-semibold">
                  {data?.username ?? "익명"}
                </span>
                :{data?.content ?? "내용x"}
              </div>
            ))}

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
