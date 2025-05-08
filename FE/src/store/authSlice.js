import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  token: "",
  isLoggedIn: !!localStorage.getItem("token"),
  user: {
    username: "",
    country: "",
  },
};
const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    login: (state, action) => {
      state.token = action.payload;
      state.isLoggedIn = true;
      localStorage.setItem("token", action.payload);
    },
    logout: (state, action) => {
      state.token = "";
      state.isLoggedIn = false;
      localStorage.removeItem("token");
    },
  },
});
export const { login, logout } = authSlice.actions;
export default authSlice.reducer;
