import React from "react";
import { RouterProvider } from "react-router-dom";
import ChatRoomList from "./ChatRoomList";
import router from "./index";
export default function App() {
  return <RouterProvider router={router} />;
}
