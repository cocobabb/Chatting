import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { useDispatch } from "react-redux";
import { login } from "../store/authSlice";

export default function Login() {
  const navigate = useNavigate();

  const dispatch = useDispatch();
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });

  // 입력되는 값 처리
  const handleInputValue = (e) => {
    const { name, value } = e.target;

    if (name == "username") {
      setFormData((prev) => ({
        ...prev,
        username: value,
      }));
    } else {
      setFormData((prev) => ({
        ...prev,
        password: value,
      }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(
        import.meta.env.VITE_API_URL + "/auth/login",
        formData,
        {
          withCredentials: true,
        }
      );
      console.log(response);
      const token = response.data.token;
      console.log(token);
      dispatch(login(token));
      navigate("/chat-rooms");
    } catch (error) {
      console.log(error);
      console.error();
    }
  };

  const formCSS = "flex flex-col";

  return (
    <div className="w-full h-screen flex items-center justify-center">
      <form
        className="bg-white shadow-lg rounded-2xl p-10 w-full max-w-md flex flex-col gap-6"
        onSubmit={handleSubmit}
      >
        <input
          className="border border-gray-300 rounded-md px-4 py-3 focus:outline-none focus:ring-2 focus:ring-green-400"
          type="text"
          name="username"
          id="username"
          placeholder="아이디"
          onChange={handleInputValue}
        />
        <input
          className="border border-gray-300 rounded-md px-4 py-3 focus:outline-none focus:ring-2 focus:ring-green-400"
          type="password"
          name="password"
          id="password"
          placeholder="비밀번호"
          onChange={handleInputValue}
        />

        <button
          className="bg-green-500 text-white rounded-md py-3 font-semibold hover:bg-green-600 transition duration-200"
          onClick={handleSubmit}
        >
          로그인
        </button>
      </form>
    </div>
  );
}
