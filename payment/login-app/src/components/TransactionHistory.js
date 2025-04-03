import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const TransactionHistory = () => {
    const [transactions, setTransactions] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchTransactionHistory = async () => {
            const token = localStorage.getItem("token");
            if (!token) {
                alert("No token found. Redirecting to login...");
                navigate("/login");
                return;
            }

            try {
                const response = await fetch("http://localhost:8080/api/user/history", {
                    method: "GET",
                    headers: {
                        "Authorization": `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                });

                if (response.ok) {
                    const data = await response.json();
                    setTransactions(data);
                } else {
                    alert("Failed to fetch transaction history!");
                }
            } catch (error) {
                alert("An error occurred while fetching transaction history.");
            } finally {
                setLoading(false);
            }
        };

        fetchTransactionHistory();
    }, [navigate]);

    if (loading) {
        return <div>Loading transaction history...</div>;
    }

    return (
        <div style={{ padding: "1rem" }}>
            <h1>Transaction History</h1>
            {transactions.length > 0 ? (
                <table border="1" style={{ width: "100%", textAlign: "left" }}>
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Type</th>
                            <th>Amount</th>
                        </tr>
                    </thead>
                    <tbody>
                        {transactions.map((transaction) => (
                            <tr key={transaction.id}>
                                <td>{new Date(transaction.date).toLocaleString()}</td>
                                <td>{transaction.type}</td>
                                <td>${transaction.amount.toFixed(2)}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            ) : (
                <p>No transactions found.</p>
            )}
            <button onClick={() => navigate("/dashboard")} style={{ marginTop: "20px" }}>
                Back to Dashboard
            </button>
        </div>
    );
};

export default TransactionHistory;
