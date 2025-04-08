import React from "react";
import { Navigate } from "react-router-dom";

const ProtectedRoute = ({ children }) => {
    const token = localStorage.getItem("token"); // Check if token exists

    // If no token, redirect to the login page
    return token ? children : <Navigate to="/login" />;
};

export default ProtectedRoute;