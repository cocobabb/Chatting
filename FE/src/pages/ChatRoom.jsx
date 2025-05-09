import axios from "axios";
import React, { useEffect, useRef, useState } from "react";
import { useLocation } from "react-router-dom";

export default function ChatRoom() {
  const token = localStorage.getItem("token");
  const wsRef = useRef(null);
  const [messages, setMessages] = useState([]);
  const scrollRef = useRef(null);
  const [input, setInput] = useState("");

  const location = useLocation();
  const { id, title } = location.state || {};

  useEffect(() => {
    if (!token) {
      navigate("/");
      return;
    }
    console.log(id);
    const ws = new WebSocket(
      `ws://${import.meta.env.VITE_WS_URL}/${id}?token=${token}`
    );
    wsRef.current = ws;

    ws.onmessage = (e) => {
      const [sender, ...rest] = e.data.split(": ");
      const text = rest.join(": ");

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
            {messages.map((msg, i) => (
              <div key={i} className="mb-2">
                <span className="font-semibold">{msg.sender}</span>: {msg.text}
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
