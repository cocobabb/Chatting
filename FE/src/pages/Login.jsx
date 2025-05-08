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

  const moveToButton = (e) => {
    if (e.key === "Enter") {
      handleSubmit();
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
    } catch (error) {
      console.log(error);
      console.error();
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="text"
        name="username"
        id="username"
        placeholder="아이디"
        onChange={handleInputValue}
      />
      <input
        type="password"
        name="password"
        id="password"
        placeholder="비밀번호"
        onChange={handleInputValue}
        onKeyDown={moveToButton}
      />

      <div>
        <button>로그인</button>
      </div>
    </form>
  );
}
