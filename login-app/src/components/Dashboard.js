import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const Dashboard = () => {
    const [accountDetails, setAccountDetails] = useState(null); // State to store account details
    const [loading, setLoading] = useState(true); // State to handle loading indicator
    const [amount, setAmount] = useState(""); // State to handle transaction amount
    const navigate = useNavigate();

    useEffect(() => {
        const fetchAccountDetails = async () => {
            const token = localStorage.getItem("token"); // Get JWT token from localStorage
            if (!token) {
                alert("No token found. Redirecting to login...");
                localStorage.removeItem("token"); // Clear invalid token
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

                if (response.ok) {
                    const data = await response.json(); // Parse the JSON response
                    setAccountDetails(data); // Save account details to state
                } else {
                    alert("Unable to fetch account details!");
                }
            } catch (error) {
                alert("An error occurred while fetching account details.");
            } finally {
                setLoading(false); // Stop loading after data fetch
            }
        };

        fetchAccountDetails();
    }, [navigate]);

    const handleTransaction = async (type) => {
        const token = localStorage.getItem("token");
        if (!token) {
            alert("No token found. Redirecting to login...");
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/api/user/${type}`, {
                method: "POST",
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ amount: parseFloat(amount) }),
            });

            if (response.ok) {
                alert(`${type.charAt(0).toUpperCase() + type.slice(1)} successful!`);
                // Refresh account details after transaction
                const updatedDetails = await fetch("http://localhost:8080/api/user/account", {
                    method: "GET",
                    headers: {
                        "Authorization": `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                });

                const data = await updatedDetails.json();
                setAccountDetails(data); // Update account details
                setAmount(""); // Clear transaction input
            } else {
                const errorMessage = await response.text();
                alert(`Transaction failed: ${errorMessage}`);
            }
        } catch (error) {
            alert("An error occurred during the transaction.");
        }
    };

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

            <div>
                <h3>Perform Transaction</h3>
                <input
                    type="number"
                    value={amount}
                    onChange={(e) => setAmount(e.target.value)}
                    placeholder="Enter amount"
                />
                <button
                    onClick={() => handleTransaction("deposit")}
                    style={{ marginRight: "10px" }}
                >
                    Deposit
                </button>
                <button onClick={() => handleTransaction("withdraw")}>Withdraw</button>
            </div>

            <div>
                            <button
                                onClick={() => navigate("/transaction-history")}
                                style={{ marginBottom: "10px" }}
                            >
                                View Transaction History
                            </button>
                        </div>

            <button
                onClick={() => {
                    localStorage.removeItem("token");
                    window.location.href = "/login"; // Logout and redirect to login page
                }}
                style={{ marginTop: "20px" }}
            >
                Logout
            </button>
        </div>
    );
};

export default Dashboard;