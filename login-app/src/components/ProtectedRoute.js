import React from "react";
import { Navigate } from "react-router-dom";

const ProtectedRoute = ({ children }) => {
    const token = localStorage.getItem("token"); // Retrieve token from localStorage

    if (!token) {
        // If no token is found, redirect to the login page
        return <Navigate to="/login" replace />;
    }

    try {
        // Decode JWT payload to extract expiration time
        const { exp } = JSON.parse(atob(token.split(".")[1])); // Extract payload from JWT
        const currentTime = Math.floor(Date.now() / 1000); // Get current time in seconds

        if (currentTime >= exp) {
            // If the token is expired, clear it and redirect to the login page
            alert("Session expired. Redirecting to login...");
            localStorage.removeItem("token");
            return <Navigate to="/login" replace />;
        }
    } catch (error) {
        // Handle invalid or malformed token
        alert("Invalid token detected. Redirecting to login...");
        localStorage.removeItem("token");
        return <Navigate to="/login" replace />;
    }

    // If token is valid and not expired, render the protected component
    return children;
};

export default ProtectedRoute;