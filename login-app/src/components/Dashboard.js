import React, { useState, useEffect, useCallback } from "react";
import { useNavigate } from "react-router-dom";

const Dashboard = () => {
    const [accountDetails, setAccountDetails] = useState(null);
    const [loading, setLoading] = useState(true);
    const [amount, setAmount] = useState("");
    const navigate = useNavigate();

    // Stabilize fetchAccountDetails using useCallback
    const fetchAccountDetails = useCallback(async () => {
        const token = localStorage.getItem("token");
        if (!token) {
            alert("No token found. Redirecting to login...");
            localStorage.removeItem("token");
            navigate("/login");
            return;
        }

        const { exp } = JSON.parse(atob(token.split(".")[1]));
        const currentTime = Math.floor(Date.now() / 1000);
        if (currentTime >= exp) {
            alert("Session expired. Redirecting to login...");
            localStorage.removeItem("token");
            navigate("/login");
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/api/user/account", {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
            });

            if (response.ok) {
                const data = await response.json();
                setAccountDetails(data);
            } else {
                alert("Unable to fetch account details!");
            }
        } catch (error) {
            alert("An error occurred while fetching account details.");
        } finally {
            setLoading(false);
        }
    }, [navigate]); // Include `navigate` as a dependency

    // Fetch account details on mount
    useEffect(() => {
        fetchAccountDetails();
    }, [fetchAccountDetails]); // Add `fetchAccountDetails` to the dependency array

    const handleTransaction = async (type) => {
        const token = localStorage.getItem("token");
        if (!token) {
            alert("No token found. Redirecting to login...");
            return;
        }

        const { exp } = JSON.parse(atob(token.split(".")[1]));
        const currentTime = Math.floor(Date.now() / 1000);
        if (currentTime >= exp) {
            alert("Session expired. Redirecting to login...");
            localStorage.removeItem("token");
            navigate("/login");
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/api/user/${type}`, {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ amount: parseFloat(amount) }),
            });

            if (response.ok) {
                alert(`${type.charAt(0).toUpperCase() + type.slice(1)} successful!`);
                fetchAccountDetails(); // Refresh account details
                setAmount(""); // Clear input
            } else {
                const errorMessage = await response.text();
                alert(`Transaction failed: ${errorMessage}`);
            }
        } catch (error) {
            alert("An error occurred during the transaction.");
        }
    };

    if (loading) {
        return <div>Loading account details...</div>;
    }

    if (!accountDetails) {
        return <div>No account details available.</div>;
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