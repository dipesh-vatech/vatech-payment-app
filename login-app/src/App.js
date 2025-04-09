import React, { useEffect, useCallback } from "react";
import { Routes, Route, Navigate, useNavigate } from "react-router-dom";
import Header from "./components/Header";
import LoginForm from "./components/LoginForm";
import RegisterForm from "./components/RegisterForm";
import Dashboard from "./components/Dashboard";
import TransactionHistory from "./components/TransactionHistory";
import ProtectedRoute from "./components/ProtectedRoute";

function App() {
    const navigate = useNavigate();

    // Function to check if the token has expired
    const checkTokenExpiry = useCallback(() => {
        const token = localStorage.getItem("token");
        if (token) {
            try {
                const { exp } = JSON.parse(atob(token.split(".")[1])); // Decode JWT payload to get expiration time
                const currentTime = Math.floor(Date.now() / 1000); // Get the current time in seconds

                // If the token has expired, log the user out and navigate to the login page
                if (currentTime >= exp) {
                    alert("Session expired. Please log in again.");
                    localStorage.removeItem("token"); // Clear the token from storage
                    navigate("/login"); // Redirect to login page
                }
            } catch (error) {
                console.error("Error while checking token expiry:", error);
                localStorage.removeItem("token"); // Clear potentially invalid token
                navigate("/login"); // Redirect to login page
            }
        }
    }, [navigate]); // Add `navigate` as a dependency since it is used in the function

    // Periodically check token expiry using `useEffect`
    useEffect(() => {
        const interval = setInterval(checkTokenExpiry, 30 * 1000); // Run the check every 30 seconds
        return () => clearInterval(interval); // Clean up the interval on component unmount
    }, [checkTokenExpiry]);

    return (
        <div>
            <Header />
            <h1>VaTech Login</h1>
            <Routes>
                <Route path="/" element={<Navigate to="/login" replace />} />
                <Route path="/login" element={<LoginForm />} />
                <Route path="/register" element={<RegisterForm />} />
                <Route
                    path="/dashboard"
                    element={
                        <ProtectedRoute>
                            <Dashboard />
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/transaction-history"
                    element={
                        <ProtectedRoute>
                            <TransactionHistory />
                        </ProtectedRoute>
                    }
                />
            </Routes>
        </div>
    );
}

export default App;