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
                const data = await response.json(); // Parse JSON response
                alert(`Registration successful! Your account has been created. You can now log in.`);
                navigate("/login"); // Redirect to login page
            } else {
                const errorData = await response.json(); // Extract detailed error message
                alert(`Registration failed: ${errorData.error || "Unknown error"}`);
                console.error("Server error:", errorData);
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