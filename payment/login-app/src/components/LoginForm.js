import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

function LoginForm() {
    // Hooks are called at the top level of the functional component
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate(); // Proper usage of hooks

    if (localStorage.getItem("token")) {
            window.location.href = "/dashboard";
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
                       const errorMessage = await response.text(); // Fetch backend error message
                       console.error(`Login failed with status ${response.status}:`, errorMessage);
                       alert(`Login failed: ${response.status} - ${errorMessage || "Invalid credentials."}`);
                   }
        } catch (error) {
            console.error("Login error:", error);
        }
    };

    const handleRegisterRedirect = () => {
            navigate("/register"); // Navigate to the registration page
        };

    return (
        <div>
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
                <button type="submit" style={{ marginRight: "10px" }}>Login</button>
                <button type="button" onClick={handleRegisterRedirect}>Register</button>
            </form>
        </div>
    );
}

export default LoginForm;