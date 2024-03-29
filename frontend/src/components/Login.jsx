import { useNavigate, useOutletContext } from "react-router-dom";
import "./Login&Register.css";
import { useEffect, useState } from "react";
import useHandleFetchError from "../hooks/useHandleFetchError";
import { useNotificationsDispatch } from "./notifications/NotificationContext";

const Login = () => {
  const { user, update, setUpdate } = useOutletContext();
  const navigate = useNavigate();
  const handleFetchError = useHandleFetchError();
  const notifDispatch = useNotificationsDispatch();
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (user !== null) {
      navigate("/");
    }
  }, [user, navigate]);

  const onSubmit = (e) => {
    setSubmitting(true);
    e.preventDefault();
    const formData = new FormData(e.target);
    const entries = [...formData.entries()];
    const credentials = entries.reduce((acc, entry) => {
      const [k, v] = entry;
      acc[k] = v;
      return acc;
    }, {});
    handleLogin(credentials);
  };

  async function handleLogin(formData) {
    try {
      const res = await fetch("/api/v1/auth/authenticate", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });
      if (res.ok) {
        const data = await res.json();
        if (data) {
          setUpdate(!update);
          navigate("/station");
        }
      } else {
        handleFetchError(res);
      }
    } catch (err) {
      console.error(err);
      notifDispatch({
        type: "generic error",
      });
    }
    setSubmitting(false);
  }

  return (
    <div className="lrform-container">
      <form onSubmit={onSubmit}>
        <h2>Login</h2>
        <input name="username" type="text" required placeholder="Username"></input>
        <input name="password" type="password" required placeholder="Password"></input>
        <button className="button" type="submit" disabled={submitting}>
          Login
        </button>
      </form>
    </div>
  );
};

export default Login;
