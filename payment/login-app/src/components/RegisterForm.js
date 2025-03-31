import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

function RegisterForm() {
    // Hooks must be called at the top level of the functional component
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const navigate = useNavigate(); // Correct usage of hooks

    const handleRegister = async (e) => {
        e.preventDefault();

        // Validate that passwords match
        if (password !== confirmPassword) {
            alert("Passwords do not match!");
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/api/auth/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username, password }),
            });

            if (response.ok) {
                alert("Registration successful! You can now log in.");
                navigate("/login"); // Redirect to login page
            } else {
                const errorMessage = await response.text();
                alert(`Registration failed: ${errorMessage}`);
                console.error("Server error:", errorMessage);
            }
        } catch (error) {
            console.error("Registration error:", error);
            alert("An error occurred during registration. Please try again later.");
        }
    };

    return (
        <div style={{ maxWidth: "400px", margin: "auto", padding: "1rem", border: "1px solid #ddd", borderRadius: "8px" }}>
            <h2>Register</h2>
            <form onSubmit={handleRegister}>
                <div>
                    <label>Username:</label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Password:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Confirm Password:</label>
                    <input
                        type="password"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Register</button>
            </form>
        </div>
    );
}

export default RegisterForm;