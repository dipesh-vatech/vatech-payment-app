import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./LoginForm.css"; // Import the CSS file

function LoginForm() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    if (localStorage.getItem("token")) {
//        window.location.href = "/dashboard";
    }

const handleLogin = async (e) => {
    e.preventDefault();
    try {
        const response = await fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password }),
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem("token", data.token);
            navigate("/dashboard");
        } else {
            const errorMessage = await response.text();
            if (response.status === 401) {
                alert("Session expired or invalid credentials. Please try again.");
            } else {
                alert(`Login failed: ${response.status} - ${errorMessage}`);
            }
        }
    } catch (error) {
        console.error("Login error:", error);
    }
};

    const handleRegisterRedirect = () => {
        navigate("/register");
    };

    return (
        <div className="login-container">
            <h2>Login</h2>
            <form onSubmit={handleLogin}>
                <div>
                    <label>Username</label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Password</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Login</button>
                <button type="button" className="secondary" onClick={handleRegisterRedirect}>
                    Register
                </button>
            </form>
        </div>
    );
}

export default LoginForm;
