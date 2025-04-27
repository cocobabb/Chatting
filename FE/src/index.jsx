import React from "react";
import { createBrowserRouter } from "react-router-dom";
import ChatRoom from "./ChatRoom";
import ChatRoomList from "./ChatRoomList";

const router = createBrowserRouter([
  {
    path: "/",
    element: <ChatRoomList />,
  },
  {
    path: "/chatroom/:chatRoomId",
    element: <ChatRoom />,
  },
]);

export default router;
