import React from "react";
import { createBrowserRouter } from "react-router-dom";
import Login from "./pages/Login";
import Home from "./Home";
import ChatRoomList from "./pages/ChatRoomList";
import ChatRoom from "./pages/ChatRoom";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Home />,
  },
  {
    path: "/login",
    element: <Login />,
  },
  {
    path: "/chat-rooms",
    children: [
      {
        index: true,
        element: <ChatRoomList />,
      },
      {
        path: ":title",
        element: <ChatRoom />,
      },
    ],
  },
]);

export default router;
