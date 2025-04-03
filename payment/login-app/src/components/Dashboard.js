import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const Dashboard = () => {
    const [accountDetails, setAccountDetails] = useState(null); // State to store account details
    const [loading, setLoading] = useState(true); // State to handle loading indicator
    const navigate = useNavigate();

    useEffect(() => {
        const fetchAccountDetails = async () => {
            const token = localStorage.getItem("token"); // Get JWT token from localStorage
            console.log("Token sent in Authorization header:", token);
            if (!token) {
                            console.warn("Token not found in localStorage.");
                            alert("No token found. Redirecting to login...");
                            localStorage.removeItem("token"); // Clear any invalid token
                            navigate("/login");
                            return;
                        }

            try {
                const response = await fetch("http://localhost:8080/api/user/account", {
                    method: "GET",
                    headers: {
                        "Authorization": `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                });
//                console.log("Headers:", {
//                    "Authorization": `Bearer ${token}`,
//                    "Content-Type": "application/json",
//                });

                if (response.ok) {
                    const data = await response.json(); // Parse the JSON response
                    console.log("Account Details Response:", data);
                    setAccountDetails(data); // Save account details to state
                } else {
                    console.error("Failed to fetch account details");
                    alert("Unable to fetch account details!");
                }
            } catch (error) {
                console.error("Error fetching account details:", error);
                alert("An error occurred while fetching account details.");
            } finally {
                setLoading(false); // Stop loading after data fetch
            }
        };

        fetchAccountDetails();
    }, [navigate]); // Only fetch account details once, on component mount

    if (loading) {
        return <div>Loading account details...</div>; // Display loading indicator
    }

    if (!accountDetails) {
        return <div>No account details available.</div>; // Display fallback if no data
    }

    return (
        <div style={{ padding: "1rem" }}>
            <h1>Welcome</h1>
            <p>Account Number: {accountDetails.accountNumber}</p>
            <p>Account Balance: ${accountDetails.accountBalance.toFixed(2)}</p>
            <button
                onClick={() => {
                    localStorage.removeItem("token");
                    window.location.href = "/login"; // Logout and redirect to login page
                }}
            >
                Logout
            </button>
        </div>
    );
};

export default Dashboard;