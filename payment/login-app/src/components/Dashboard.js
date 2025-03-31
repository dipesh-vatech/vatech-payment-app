import React from "react";

const Dashboard = () => {
    const handleLogout = () => {
        // Remove token from localStorage
        localStorage.removeItem("token");

        // Redirect to the login page
        window.location.href = "/login";
    };

    return (
        <div style={{ padding: "1rem", textAlign: "center" }}>
            <h1>Welcome to Your Dashboard</h1>
            <button onClick={handleLogout} style={{ marginTop: "20px", padding: "10px 20px" }}>
                Logout
            </button>
        </div>
    );
};

export default Dashboard;