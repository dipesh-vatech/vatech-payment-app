import React from "react";
import { useLocation } from "react-router-dom";

const Header = () => {
    const location = useLocation(); // Get current route

    const handleLogout = () => {
        localStorage.removeItem("token"); // Remove JWT token
        window.location.href = "/login"; // Redirect to login page
    };

    // Hide the header on the login page
    if (location.pathname === "/login") {
        return null;
    }

    return (
        <div
            style={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                padding: "1rem",
                borderBottom: "1px solid #ddd",
                backgroundColor: "#f8f9fa",
                position: "sticky",
                top: 0,
                zIndex: 1000,
            }}
        >
            <h2>My App</h2>
            <button
                onClick={handleLogout}
                style={{
                    padding: "10px 20px",
                    border: "none",
                    borderRadius: "5px",
                    backgroundColor: "#dc3545",
                    color: "white",
                    cursor: "pointer",
                }}
            >
                Logout
            </button>
        </div>
    );
};

export default Header;